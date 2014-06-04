package com.jrew.lab.patcher.shell;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.06.14
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class Shell {

    private static final String JAR_PATH = System.getenv("MAVEN_REPOSITORY") + "/com/jrew/lab/java-runtime-patcher/1.0-SNAPSHOT/" +
            "java-runtime-patcher-1.0-SNAPSHOT.jar";

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException,
            AgentInitializationException {

        System.out.println("Java runtime patcher utility");
        System.out.println();
        System.out.println("Please select JVM process to patch:");

        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        for (int i = 0; i < virtualMachineDescriptors.size(); i++) {

            VirtualMachineDescriptor virtualMachineDescriptor =  virtualMachineDescriptors.get(i);
            System.out.println();
            System.out.println((i + 1) +") PID = " + virtualMachineDescriptor.id());
            System.out.println(virtualMachineDescriptor.displayName());
        }

        System.out.println();
        System.out.println("Enter choice number...");

        String choice = null;
        Scanner scanner = new Scanner(System.in);
        choice = scanner.nextLine();
        scanner.close();

        System.out.println();
        System.out.println("Selected option: " + choice);

        int selectedOption = Integer.valueOf(choice);
        if (selectedOption - 1 < virtualMachineDescriptors.size()) {
            VirtualMachineDescriptor selectedVirtualMachineDescriptor = virtualMachineDescriptors.get(selectedOption - 1);
            VirtualMachine virtualMachine = VirtualMachine.attach(selectedVirtualMachineDescriptor.id());
            virtualMachine.loadAgent(JAR_PATH);
            virtualMachine.detach();
        }
    }

}
