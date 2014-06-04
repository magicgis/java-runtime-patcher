package com.jrew.lab.patcher.shell;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.06.14
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class Sandbox {

    /** **/
    private final static String PROCESS_NAME = "Sandbox";

    /** **/
    private final static String AGENT_JAR_PATH = System.getProperty("user.home") + "/.m2/repository/com/jrew/lab/java-agent/" +
            "1.0-SNAPSHOT/java-agent-1.0-SNAPSHOT-jar-with-dependencies.jar";

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException,
            AgentInitializationException {

        String processPid = null;

        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        for (VirtualMachineDescriptor virtualMachineDescriptor : virtualMachineDescriptors) {

            System.out.println();
            System.out.println(virtualMachineDescriptor.displayName());
            System.out.println(virtualMachineDescriptor.id());
            System.out.println();

            if (virtualMachineDescriptor.displayName().contains(PROCESS_NAME)) {
                processPid = virtualMachineDescriptor.id();
            }
        }

        if (processPid != null) {
            VirtualMachine virtualMachine = VirtualMachine.attach(processPid);
            // Put current class name as parameter to agent
            virtualMachine.loadAgent(AGENT_JAR_PATH, Sandbox.class.getName());
            virtualMachine.detach();
        }
    }

    /**
     *
     * @param importantParameter
     * @return
     */
    private int testPrivateMethod(String importantParameter) {
        return 0;
    }

    /**
     *
     * @param inputParameters
     * @return
     */
    public List<String> testPublicMethod(Map<String, String> inputParameters) {
        return null;
    }
}
