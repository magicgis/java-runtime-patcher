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
    public static void main(String[] args) throws IOException {

        Shell shell = new Shell();
        shell.run();
    }

    /**
     *
     */
    private void run() {

        ShellEcho.printGreetingsMessage();
        processJVMSelection();

    }

    /**
     *
     */
    private void processJVMSelection() {

        ShellEcho.printJVMSelectionMessage();

        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        printJVMs(virtualMachineDescriptors);

        runJVMSelectionLoop(virtualMachineDescriptors);
    }

    /**
     *
     * @param virtualMachineDescriptors
     */
    private void printJVMs(List<VirtualMachineDescriptor> virtualMachineDescriptors) {
        for (int i = 0; i < virtualMachineDescriptors.size(); i++) {
            VirtualMachineDescriptor virtualMachineDescriptor =  virtualMachineDescriptors.get(i);
            ShellEcho.printJVMDescriptionMessage(i + 1, virtualMachineDescriptor);
        }
    }

    /**
     *
     * @param virtualMachineDescriptors
     */
    private void runJVMSelectionLoop(List<VirtualMachineDescriptor> virtualMachineDescriptors) {

        boolean isAgentApplied = false;
        Scanner scanner = new Scanner(System.in);

        while (!isAgentApplied) {

            ShellEcho.printJVMSelectionRequest();
            String choice = scanner.nextLine();

            if (validateEnteredChoice(choice, virtualMachineDescriptors.size())) {

                ShellEcho.printJVMSelectedChoice(choice);

                int selectedOption = Integer.parseInt(choice) - 1;
                isAgentApplied = runAgent(virtualMachineDescriptors.get(selectedOption));
            }
        }

        scanner.close();
    }

    /**
     *
     * @param choice
     * @return
     */
    private boolean validateEnteredChoice(String choice, int possibleRange) {

        try {

            int selectedOption = Integer.valueOf(choice);
            if (selectedOption - 1 >= 0 && selectedOption < possibleRange) {
                return true;
            } else {
                ShellEcho.printWrongChoiceSelectionMessage();
                return false;
            }

        } catch (NumberFormatException exception) {
            ShellEcho.printWrongNumberMessage(choice);
        }

        return false;
    }

    /**
     *
     * @param virtualMachineDescriptor
     */
    private boolean runAgent( VirtualMachineDescriptor virtualMachineDescriptor) {

        try {

           VirtualMachine virtualMachine = VirtualMachine.attach(virtualMachineDescriptor.id());
           virtualMachine.loadAgent(JAR_PATH);
           virtualMachine.detach();

           return true;

       }  catch (AttachNotSupportedException attachException) {
        ShellEcho.printJVMAttachIssueMessage(attachException);
        } catch (AgentLoadException loadException) {
            ShellEcho.printAgentIssueMessage(loadException);
        } catch (AgentInitializationException initException) {
            ShellEcho.printAgentIssueMessage(initException);
        } catch (IOException exception) {
            ShellEcho.printJVMAttachIssueMessage(exception);
        }

        return false;
    }
}
