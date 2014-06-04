package com.jrew.lab.patcher.agent;

import com.jrew.lab.patcher.agent.reader.ClassPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

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

//        instrumentation.addTransformer(new ClassFileTransformer() {
//
//            @Override
//            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
//                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//
//                if (loader != null) {
//                    System.out.println("Classloader: " + loader);
//                }
//
//                if (classBeingRedefined != null) {
//                    System.out.println("Class: " + classBeingRedefined.getName());
//                }
//
//                ClassReader classReader = new ClassReader(classfileBuffer);
//                ClassVisitor classPrinter = new ClassPrinter();
//                classReader.accept(classPrinter, 0);
//
//                return classfileBuffer;
//            }
//        });

        Class[] loadedClasses = instrumentation.getAllLoadedClasses();
        for (Class loadedClass : loadedClasses) {

            System.out.println();
            System.out.println(loadedClass.getCanonicalName());
            System.out.println();

           try {

               ClassReader classReader = new ClassReader(loadedClass.getName());
               ClassVisitor classPrinter = new ClassPrinter();
               classReader.accept(classPrinter, 0);

           } catch (Exception exception) {
               exception.printStackTrace();
           }


        }
    }
}
