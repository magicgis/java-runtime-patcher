package com.jrew.lab.patcher.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.06.14
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class Agent {

    public static void premain(String args, Instrumentation instrumentation) {

    }

    public static void agentmain(String args, Instrumentation instrumentation) throws IOException {

        System.out.println("Inside of Agent main method...");

//        Class[] loadedClasses = instrumentation.getAllLoadedClasses();
//        for (Class loadedClass : loadedClasses) {
//
//        }
    }
}
