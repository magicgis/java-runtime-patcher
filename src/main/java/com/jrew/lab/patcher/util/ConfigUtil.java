package com.jrew.lab.patcher.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.06.14
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class ConfigUtil {

    /** **/
    private static final String CONFIG_FILE_NAME = "config.properties";

    /**
     *
     */
    interface PropertiesKey {

        /** **/
        String PATH_FOLDER_KEY = "patch.folder.path";

        /** **/
        String PATH_TO_JAR_KEY = "jar.path";

    }

    /**
     *
     */
    interface PropertiesValues {

        /** **/
        String CURRENT_JAR_PATH_PLACEHOLDER = "jar.current.path";

    }

    /** **/
    private static ConfigUtil instance;

    /** **/
    private Configuration configuration;

    static {
        instance = new ConfigUtil();
    }

    /**
     *
     * @return
     */
    public static ConfigUtil getInstance() {
        return instance;
    }

    /**
     *
     */
    private ConfigUtil() {
        try {
            configuration = new PropertiesConfiguration(CONFIG_FILE_NAME);
        } catch (ConfigurationException exception) {
            throw new RuntimeException("Can't load config.properties file. ", exception);
        }
    }

    /**
     *
     * @return
     * @throws ConfigurationException
     */
    public String getPatchFolderPath() {
        Path path = Paths.get(configuration.getString(PropertiesKey.PATH_FOLDER_KEY));
        return path.toAbsolutePath().toString();
    }

    /**
     *
     * @return
     * @throws ConfigurationException
     */
    public String getJarPath() {

        String jarPath = configuration.getString(PropertiesKey.PATH_TO_JAR_KEY);
        if (StringUtils.isEmpty(jarPath) || jarPath.equalsIgnoreCase(PropertiesValues.CURRENT_JAR_PATH_PLACEHOLDER)) {
            return getJarCurrentPath();
        }

        return jarPath;
    }

    /**
     *
     * @return
     */
    private String getJarCurrentPath() {
        String path = ConfigUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            File jarFile = new File(URLDecoder.decode(path, "UTF-8"));
            return jarFile.getAbsolutePath();
        } catch (UnsupportedEncodingException exception) {
            throw new RuntimeException("Couldn't detect jar path: " + exception.getMessage());
        }
    }
}
