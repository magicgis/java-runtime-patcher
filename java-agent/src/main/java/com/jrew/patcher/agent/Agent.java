package com.jrew.patcher.agent;

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

    public static void agentmain(String args, Instrumentation instrumentation) {
        System.out.println("Inside of agentmain");

    }
}
