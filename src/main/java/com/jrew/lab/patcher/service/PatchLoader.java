package com.jrew.lab.patcher.service;

import com.jrew.lab.patcher.util.ClassFilesUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.06.14
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class PatchLoader {

    /** **/
    private Logger logger = LoggerFactory.getLogger(PatchLoader.class);

    /** **/
    private static final String PATCH_FOLDER_NAME = "patch";

    /** **/
    private static final String PATCH_FOLDER_PATH = "d:/temp/000";

    /**
     *
     * @return
     */
    public Map<String, byte[]> loadPatchClasses() {

        logger.info("Started patch classes loading...{}", System.getProperty("line.separator"));
        Map<String, byte[]> patchClasses = new HashMap<String, byte[]>();

        File[] classFiles = loadClassFiles();
        for (File classFile : classFiles) {

            logger.info("Loading file: {}", classFile.getName());
            try {

                byte[] fileData = FileUtils.readFileToByteArray(classFile);
                String classFullName = ClassFilesUtil.readClassFileName(fileData);

                logger.info("Full class name: {}{}", classFullName, System.getProperty("line.separator"));
                patchClasses.put(classFullName, fileData);

            } catch (IOException e) {
                logger.info("{}Can't read file: {}{}", System.getProperty("line.separator"), classFile.getName(),
                        System.getProperty("line.separator"));
            }
        }

        return patchClasses;
    }

    /**
     *
     * @return
     */
    private File[] loadClassFiles() {

        File patchFolder = new File(PATCH_FOLDER_PATH);

        if (patchFolder.isDirectory()) {

            return patchFolder.listFiles();
        }

        throw new IllegalArgumentException("Provided path isn't directory");
    }
}
