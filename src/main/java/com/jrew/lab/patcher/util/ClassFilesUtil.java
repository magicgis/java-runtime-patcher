package com.jrew.lab.patcher.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.06.14
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class ClassFilesUtil {

    static class ClassNameReader extends ClassVisitor {

        /** **/
        private String className;

        public ClassNameReader() {
            super(Opcodes.ASM5);
        }

        @Override
        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            className = name.replaceAll("/", ".");
        }

        /**
         *
         * @return
         */
        String getClassName() {
            return className;
        }
    }

    /**
     *
     * @param fileData
     * @return
     */
    public static String readClassFileName(byte[] fileData) {

        ClassReader classReader = new ClassReader(fileData);
        ClassNameReader classNameReader = new ClassNameReader();
        classReader.accept(classNameReader, 0);

        return classNameReader.getClassName();
    }
}
