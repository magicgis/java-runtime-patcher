package com.jrew.lab.patcher.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

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

        String PATH_TO_JAR_KEY = "jar.path";
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

        return configuration.getString(PropertiesKey.PATH_FOLDER_KEY);
    }

    /**
     *
     * @return
     * @throws ConfigurationException
     */
    public String getJarPath() {

        return configuration.getString(PropertiesKey.PATH_TO_JAR_KEY);
    }

}
