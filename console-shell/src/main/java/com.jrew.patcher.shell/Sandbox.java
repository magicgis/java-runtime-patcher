package com.jrew.patcher.shell;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.06.14
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class Sandbox {

    private final static String PROCESS_NAME = "Sandbox";

    /**
     *
     * @param args
     */
    public static void main(String[] args) throws IOException, AttachNotSupportedException {

        String processPid = null;

        List<VirtualMachineDescriptor> virtualMachineDescriptors = VirtualMachine.list();
        for (VirtualMachineDescriptor virtualMachineDescriptor : virtualMachineDescriptors) {
            if (virtualMachineDescriptor.displayName().contains(PROCESS_NAME)) {
                processPid = virtualMachineDescriptor.id();
            }
        }

        if (processPid != null) {
            VirtualMachine virtualMachine = VirtualMachine.attach(processPid);
        }
    }
}
