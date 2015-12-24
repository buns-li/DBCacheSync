package core;

import utils.XmlUtils;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/12/23.
 * <p>
 * 存放当前服务的基本配置信息和一些全局数据缓存
 */
public class PlatGlobal {

    private Map<String, Object> configCaches;

    /**
     * 用于存储当前服务的一些系统设置内容（主要数据来自于platform.properties文件中的配置信息)
     */
    private Properties platformProperties;

    /**
     * 获取当前项目的根目录路径
     */
    private String projectRootPath = System.getProperty("user.dir");

    private PlatGlobal() {
        if (platformProperties == null) {
            //先加载platform.properties内的平台相关信息设置，并将这些配置信息放在全局对象中
            platformProperties = new Properties();
            URL fileUrl = ClassLoader.getSystemResource("platform.properties");
            try {
                platformProperties.load(new FileReader(fileUrl.getFile()));
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println(ex);
            }
        }
        if (configCaches == null) {
            configCaches = new HashMap<>();
        }
    }

    private static class SingletonHolder {
        public final static PlatGlobal instance = new PlatGlobal();
    }

    public static PlatGlobal instance() {
        return SingletonHolder.instance;
    }

    /**
     * 获取系统设置存储对象
     *
     * @return
     */
    public Properties getPlatformConfig() {
        return platformProperties;
    }

    /**
     * 获取系统配置项内容属性
     * @return
     */
    public Map<String, Object> getConfigCaches() {
        return configCaches;
    }

    /**
     * 获取项目根路径
     *
     * @return
     */
    public String getProjectRootPath() {
        return projectRootPath;
    }

    /**
     * 预解析配置文件,并且加入全局的缓存对象中
     *
     * @param configPath 配置文件地址
     */
    public PlatGlobal preAnalysicConfig(String configPath) {
        URL fileUrl = ClassLoader.getSystemResource(configPath);

        if (fileUrl == null) return this;

        return this;
    }

}