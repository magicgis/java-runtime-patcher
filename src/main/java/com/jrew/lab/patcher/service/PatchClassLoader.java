package com.jrew.lab.patcher.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/8/2014
 * Time: 8:29 PM
 */
public class PatchClassLoader extends ClassLoader {

    public PatchClassLoader() {
    }

    public PatchClassLoader(ClassLoader parent) {
        super(parent);
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {

        PatchReader patchReader = new PatchReader();
        Map<String, byte[]> patchClassesData = patchReader.loadPatchClasses();
        if (patchClassesData.containsKey(className)) {
            byte[] classBinaryData = patchClassesData.get(className);
            return defineClass(className, classBinaryData, 0, classBinaryData.length);
        }

        return super.findClass(className);
    }
}
