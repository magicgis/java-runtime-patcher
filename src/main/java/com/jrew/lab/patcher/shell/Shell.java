package com.jrew.lab.patcher.shell;

import com.jrew.lab.patcher.util.ConfigUtil;
import com.sun.tools.attach.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /** **/
    private Logger logger = LoggerFactory.getLogger(Shell.class);

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

        logger.info("{}Java runtime patcher utility{}", System.getProperty("line.separator"), System.getProperty("line.separator"));
        processJVMSelection();
    }

    /**
     *
     */
    private void processJVMSelection() {

        logger.info("Please select JVM process to patch:{}", System.getProperty("line.separator"));

        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        displayAvailableJavaProcesses(virtualMachineDescriptors);

        runJVMSelectionLoop(virtualMachineDescriptors);
    }

    /**
     *
     * @param virtualMachineDescriptors
     */
    private void displayAvailableJavaProcesses(List<VirtualMachineDescriptor> virtualMachineDescriptors) {
        for (int i = 0; i < virtualMachineDescriptors.size(); i++) {
            VirtualMachineDescriptor virtualMachineDescriptor =  virtualMachineDescriptors.get(i);
            logger.info("{}) {}{}", i + 1, virtualMachineDescriptor.displayName(), System.getProperty("line.separator"));
        }
    }

    /**
     *
     * @param virtualMachineDescriptors
     */
    private void runJVMSelectionLoop(List<VirtualMachineDescriptor> virtualMachineDescriptors) {

        boolean isAgentProcessed = false;
        Scanner scanner = new Scanner(System.in);

        while (!isAgentProcessed) {

            logger.info("Enter choice number...");
            String choice = scanner.nextLine();

            if (validateEnteredChoice(choice, virtualMachineDescriptors.size())) {

                logger.info("Selected option: {}{}", choice, System.getProperty("line.separator"));
                int selectedOption = Integer.parseInt(choice) - 1;
                isAgentProcessed = runAgent(virtualMachineDescriptors.get(selectedOption));
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

            int selectedOption = Integer.valueOf(choice) - 1;
            if (selectedOption >= 0 && selectedOption < possibleRange) {
                return true;
            } else {
                logger.info("Please enter valid choice number.{}", System.getProperty("line.separator"));
                return false;
            }

        } catch (NumberFormatException exception) {
            logger.error("{} isn't valid number. Please re-enter choice. {}", choice, System.getProperty("line.separator"));
        }

        return false;
    }

    /**
     *
     * @param virtualMachineDescriptor
     */
    private boolean runAgent( VirtualMachineDescriptor virtualMachineDescriptor) {

        try {

           logger.info("Attaching to selected process...{}", System.getProperty("line.separator"));

           VirtualMachine virtualMachine = VirtualMachine.attach(virtualMachineDescriptor.id());
           virtualMachine.loadAgent(ConfigUtil.getInstance().getJarPath());
           virtualMachine.detach();

           logger.info("Java runtime patching is finished.{}", System.getProperty("line.separator"));

           return true;

        } catch (AttachNotSupportedException attachException) {
            logger.error("Couldn't attach to selected java process: {}{}", attachException.getMessage(), System.getProperty("line.separator"));
        } catch (AgentLoadException loadException) {
            logger.error("Couldn't run java agent: {}{}", loadException.getMessage(), System.getProperty("line.separator"));
        } catch (AgentInitializationException initException) {
            logger.error("Couldn't run java agent: {}{}", initException.getMessage(), System.getProperty("line.separator"));
        } catch (IOException ioException) {
            logger.error("Couldn't attach to selected java process: {}{}", ioException.getMessage(), System.getProperty("line.separator"));
        }

        return false;
    }
}
