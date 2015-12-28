package dao;

import dao.po.EDriverType;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by buns on 12/26/15.
 * 数据库连接对象管理
 */
public final class ConnectionManager {
    /**
     * 线程内共享Connection，ThreadLocal通常是全局的，支持泛型
     */
    private static ThreadLocal<DataSourceMap> threadLocal = new ThreadLocal<>();

    /**
     * 注册缓存
     *
     * @param driverType
     * @param poolConfigPath
     */
    public static void registerDataSource(EDriverType driverType, String poolConfigPath) {
        DataSourceMap dataSourceMap = new DataSourceMap();
        dataSourceMap.setDs(driverType, createDataSourceFromProperties(poolConfigPath));
        threadLocal.set(dataSourceMap);
    }

    /**
     * 构建DataSource从配置文件中
     *
     * @param poolConfigPath
     * @return
     */
    private static DataSource createDataSourceFromProperties(String poolConfigPath) {

        Properties properties = new Properties();

        URL fileUrl = ClassLoader.getSystemResource(poolConfigPath);

        try {
            properties.load(new FileReader(fileUrl.getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PoolProperties p = new PoolProperties();
        p.setUrl(String.format(properties.getProperty("url"),
                properties.getProperty("host"),
                Integer.parseInt(properties.getProperty("port")),
                properties.getProperty("database")));

        p.setUsername(properties.getProperty("userName"));
        p.setPassword(properties.getProperty("password"));

        p.setDriverClassName(properties.getProperty("driverClass"));

        p.setInitialSize(Integer.parseInt(properties.getProperty("initialSize")));
        p.setMaxActive(Integer.parseInt(properties.getProperty("maxActive")));
        p.setMinIdle(Integer.parseInt(properties.getProperty("minIdle")));
        p.setMaxIdle(Integer.parseInt(properties.getProperty("maxIdle")));

        //心跳重新连接
        p.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("testWhileIdle")));
        p.setValidationQuery(properties.getProperty("validationQuery"));
        p.setValidationInterval(Integer.parseInt(properties.getProperty("validationInterval")));

        //拦截器
        p.setJdbcInterceptors(properties.getProperty("jdbcInterceptors"));

        //回收空连接
        p.setRemoveAbandoned(Boolean.parseBoolean(properties.getProperty("removeAbandoned")));
        p.setRemoveAbandonedTimeout(Integer.parseInt(properties.getProperty("removeAbandonedTimeout")));
        p.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("timeBetweenEvictionRunsMillis")));

        //其他
        p.setJmxEnabled(Boolean.parseBoolean(properties.getProperty("jmxEnabled")));
        p.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("testOnBorrow")));
        p.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("testOnReturn")));
        p.setLogAbandoned(Boolean.parseBoolean(properties.getProperty("logAbandoned")));
        p.setMaxWait(Integer.parseInt(properties.getProperty("maxWait")));
        p.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("minEvictableIdleTimeMillis")));

        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);

        return datasource;
    }

    /**
     * 获取当前类型数据库的连接对象
     *
     * @param eDriverType
     * @return
     */
    public static Connection getConnection(EDriverType eDriverType) {
        Connection conn = null;
        // 获取当前线程内共享的DataSourceMap
        DataSourceMap dsMap = threadLocal.get();
        try {
            if (dsMap != null) {
                conn = dsMap.getDs(EDriverType.MYSQL).getConnection();
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭当前数据库连接
     */
    public static void close(Connection conn) {
        try {
            // 判断是否已经关闭
            if (conn != null && !conn.isClosed()) {
                // 关闭资源
                conn.close();
            }
        } catch (SQLException e) {
            // 异常处理
            e.printStackTrace();
        }
    }

    /**
     * 事务回滚
     *
     * @param conn
     * @param savepoint
     */
    public static void rollback(Connection conn, Savepoint savepoint) {
        try {
            // 判断是否已经关闭
            if (conn != null && !conn.isClosed()) {
                if (savepoint != null) {
                    conn.rollback(savepoint);
                } else {
                    // 关闭资源
                    conn.rollback();
                }
            }
        } catch (SQLException e) {
            // 异常处理
            e.printStackTrace();
        }
    }

    final static class DataSourceMap {
        private static Map<EDriverType, DataSource> dataSourceMap;

        static {
            dataSourceMap = new HashMap<>(3);
        }

        public DataSource getDs(EDriverType eDriverType) {
            return dataSourceMap.get(eDriverType);
        }

        public void setDs(EDriverType eDriverType, DataSource ds) {
            dataSourceMap.put(eDriverType, ds);
        }
    }
}
