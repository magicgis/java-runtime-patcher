package com.jrew.lab.patcher.agent;

import com.jrew.lab.patcher.service.PatchClassLoader;
import com.jrew.lab.patcher.service.PatchReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.06.14
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class Agent {

    /** **/
    private Logger logger = LoggerFactory.getLogger(Agent.class);

    /** **/
    private Map<String, byte[]> patchClassesData;

    public static void premain(String args, Instrumentation instrumentation) {

    }

    public static void agentmain(String args, Instrumentation instrumentation) {

        Agent agent = new Agent();
        agent.applyPatch(instrumentation);
    }

    /**
     *
     * @param instrumentation
     * @throws IOException
     */
    private void applyPatch(Instrumentation instrumentation) {

        PatchReader patchReader = new PatchReader();
        patchClassesData = patchReader.loadPatchClasses();

        List<ClassDefinition> classDefinitionsToRedefine = processClassDefinitions(instrumentation.getAllLoadedClasses());

        if(classDefinitionsToRedefine.size() > 0) {
            logger.info("Started redefining following classes: {}", System.getProperty("line.separator"));

            for (ClassDefinition classDefinition : classDefinitionsToRedefine) {
                logger.info("{}", classDefinition.getDefinitionClass().getName());
            }

            try {
                instrumentation.redefineClasses(classDefinitionsToRedefine.toArray(new ClassDefinition[classDefinitionsToRedefine.size()]));
            } catch (ClassNotFoundException exception) {
                logger.error("{}Couldn't find class on runtime: {}{}", System.getProperty("line.separator"),
                        exception.getMessage() , System.getProperty("line.separator"));
            } catch (UnmodifiableClassException exception) {
                logger.error("{}Couldn't redefine class on runtime: {}{}", System.getProperty("line.separator"),
                        exception.getMessage() , System.getProperty("line.separator"));
            }

            logger.info("{}Class redefining is finished. ", System.getProperty("line.separator"));
        } else {
            logger.info("{}Class definitions list is empty. There is nothing to redefine on runtime. ", System.getProperty("line.separator"));
        }
    }

    /**
     *
     * @return
     */
    private List<ClassDefinition> processClassDefinitions(Class[] allLoadedClasses) {

        PatchClassLoader patchClassLoader = new PatchClassLoader(patchClassesData);

        List<ClassDefinition> classDefinitions = new ArrayList<ClassDefinition>();
        for (Map.Entry<String, byte[]> patchClassEntry : patchClassesData.entrySet()) {

            String className = patchClassEntry.getKey();
            Class<Object> clazz = loadedClass(className, allLoadedClasses);
            if (clazz != null) {
                ClassDefinition patchClassDefinition = new ClassDefinition(clazz,
                        patchClassEntry.getValue());
                classDefinitions.add(patchClassDefinition);
            } else {
                // Class metadata hasn't been loaded into JVM yet
                // Use custom ClassLoader to load it
                try {
                    patchClassLoader.loadClass(className);
                    logger.error("Class {} metadata has been loaded. {}", className, System.getProperty("line.separator"));
                } catch (ClassNotFoundException exp) {
                    logger.error("Couldn't load class metadata: {}{}", exp.getMessage(), System.getProperty("line.separator"));
                }
            }
        }

        return classDefinitions;
    }

    private <T> Class<T> loadedClass(String className, Class[] allLoadedClasses) {

        for (Class loadedClass : allLoadedClasses) {
            if (loadedClass.getName().equalsIgnoreCase(className)) {
                return loadedClass;
            }
        }

        return null;
    }
}
