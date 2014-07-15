package com.jrew.lab.patcher.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/8/2014
 * Time: 8:29 PM
 */
public class PatchClassLoader extends ClassLoader {

    /** **/
    private Map<String, byte[]> patchClassesData;

    public PatchClassLoader(Map<String, byte[]> patchClassesData) {
        this.patchClassesData = patchClassesData;
    }

    public PatchClassLoader(ClassLoader parent, Map<String, byte[]> patchClassesData) {
        super(parent);
        this.patchClassesData = patchClassesData;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {

        if (patchClassesData.containsKey(className)) {
            byte[] classBinaryData = patchClassesData.get(className);
            return defineClass(className, classBinaryData, 0, classBinaryData.length);
        }

        return super.findClass(className);
    }
}
