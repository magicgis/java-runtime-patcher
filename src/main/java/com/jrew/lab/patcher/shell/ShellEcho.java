package com.jrew.lab.patcher.shell;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.06.14
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
public class ShellEcho {

    /**
     *
     */
    public static void printGreetingsMessage() {
        System.out.println("Java runtime patcher utility");
        System.out.println();
    }

    /**
     *
     */
    public static void printJVMSelectionMessage() {
        System.out.println("Please select JVM process to patch:");
    }

    /**
     *
     */
    public static void printJVMDescriptionMessage(int index, VirtualMachineDescriptor virtualMachineDescriptor) {
        System.out.println();
        System.out.println(index +") " + virtualMachineDescriptor.displayName());
    }

    /**
     *
     */
    public static void printJVMSelectionRequest() {
        System.out.println();
        System.out.println("Enter choice number...");
    }

    /**
     *
     * @param choice
     */
    public static void printJVMSelectedChoice(String choice) {
        System.out.println();
        System.out.println("Selected option: " + choice);
    }

    /**
     *
     * @param enteredValue
     */
    public static void printWrongNumberMessage(String enteredValue) {
        System.out.println();
        System.out.println("\"" + enteredValue + "\"" + " isn't valid number. Please re-enter choice.");
    }

    /**
     *
     * @param exception
     */
    public static void printJVMAttachIssueMessage(Exception exception) {
        System.out.println();
        System.out.println("Couldn't attach to selected java process: " + exception.getMessage());
    }

    /**
     *
     * @param exception
     */
    public static void printAgentIssueMessage(Exception exception) {
        System.out.println();
        System.out.println("Couldn't run java agent: " + exception.getMessage());
    }

    /**
     *
     */
    public static void printWrongChoiceSelectionMessage(){
        System.out.println();
        System.out.println("Please enter valid choice number...");
    }

}
