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
        Map<String,byte[]> patchClassesData = patchLoader.loadPatchClasses();
        List<ClassDefinition> classDefinitions = prepareClassDefinitions(patchClassesData);

        if(classDefinitions.size() > 0) {
            System.out.println();
            System.out.println("");
            logger.info("{}Started redefining for following classes: {}", System.getProperty("line.separator"), System.getProperty("line.separator"));

            for (ClassDefinition classDefinition : classDefinitions) {
                logger.info("{}", classDefinition.getClass().getName());
            }


            try {
                instrumentation.redefineClasses(classDefinitions.toArray(new ClassDefinition[classDefinitions.size()]));
            } catch (ClassNotFoundException exception) {
                logger.info("{}Couldn't find class on runtime: {}{}", System.getProperty("line.separator"),
                        exception.getMessage() , System.getProperty("line.separator"));
            } catch (UnmodifiableClassException exception) {
                logger.info("{}Could redefine class on runtime: {}{}", System.getProperty("line.separator"),
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
    private List<ClassDefinition> prepareClassDefinitions( Map<String,byte[]> patchClassesData) {

        List<ClassDefinition> classDefinitions = new ArrayList<ClassDefinition>();
        for (Map.Entry<String, byte[]> patchClassEntry : patchClassesData.entrySet()) {

            try {
                ClassDefinition patchClassDefinition = new ClassDefinition(Class.forName(patchClassEntry.getKey()), patchClassEntry.getValue());
                classDefinitions.add(patchClassDefinition);
            } catch (ClassNotFoundException e) {
                logger.info("Could find on runtime following loaded class: {}", patchClassEntry.getKey());
            }
        }

        return classDefinitions;
    }
}
