package core;

import utils.XmlUtils;

import java.io.FileReader;
import java.io.IOException;
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

    private Map<Class<?>, Object> configCaches;

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
            }
        }
        if (configCaches == null) {
            configCaches = new HashMap<>();
        }
    }

    public static PlatGlobal instance() {
        return SingletonHolder.instance;
    }

    /**
     * 获取系统设置存储对象
     *
     * @return Properties
     */
    final public Properties getPlatformConfig() {
        return platformProperties;
    }

    /**
     * 获取项目根路径
     * @return String
     */
    final public String getProjectRootPath() {
        return projectRootPath;
    }

    /**
     * 预解析配置文件,并且加入全局的缓存对象中
     * @param configPath 配置文件地址
     */
    public <T extends IConfigPOBase> PlatGlobal preAnalysicConfig(String configPath, Class<T> source) {
        URL fileUrl = ClassLoader.getSystemResource(configPath);

        if (fileUrl == null) return this;

        T instance = XmlUtils.deserialize(source, fileUrl.getPath());

        if (instance != null) {
            configCaches.put(source, instance);
        }

        return this;
    }

    /**
     * 获取当前系统存储的缓存对象
     *
     * @param targetModel 配置对象的class
     * @param <T>         具体对应的配置目标的类型
     * @return 缓存模型对象
     */
    public <T extends IConfigPOBase> T getCacheModels(Class<T> targetModel) {
        return (T) configCaches.get(targetModel);
    }

    private static class SingletonHolder {
        public final static PlatGlobal instance = new PlatGlobal();
    }
}