package com.jrew.lab.patcher.agent;

import com.jrew.lab.patcher.service.PatchLoader;
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

        PatchLoader patchLoader = new PatchLoader();
        Map<String, byte[]> patchClassesData = patchLoader.loadPatchClasses();
        List<ClassDefinition> classDefinitions = prepareClassDefinitions(patchClassesData, instrumentation.getAllLoadedClasses());

        if(classDefinitions.size() > 0) {
            logger.info("Started redefining following classes: {}", System.getProperty("line.separator"));

            for (ClassDefinition classDefinition : classDefinitions) {
                logger.info("{}", classDefinition.getDefinitionClass().getName());
            }

            try {
                instrumentation.redefineClasses(classDefinitions.toArray(new ClassDefinition[classDefinitions.size()]));
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
     * @param patchClassesData
     * @return
     */
    private List<ClassDefinition> prepareClassDefinitions( Map<String, byte[]> patchClassesData, Class[] allLoadedClasses) {

        List<ClassDefinition> classDefinitions = new ArrayList<ClassDefinition>();
        for (Map.Entry<String, byte[]> patchClassEntry : patchClassesData.entrySet()) {

            Class<Object> clazz = loadedClass(patchClassEntry.getKey(), allLoadedClasses);
            if (clazz != null) {
                ClassDefinition patchClassDefinition = new ClassDefinition(clazz,
                        patchClassEntry.getValue());
                classDefinitions.add(patchClassDefinition);
            } else {
                logger.error("Couldn't find on runtime loaded class: {}", patchClassEntry.getKey());
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
