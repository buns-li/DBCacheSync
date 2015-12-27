import core.PlatGlobal;
import models.DbCacheConfigModel;
import models.QueryLogicConfigModel;
import org.apache.log4j.PropertyConfigurator;
import servers.cacheServer.CacheServer;
import servers.codeServer.CodeServer;
import servers.mqServer.MessageQueueServer;

/**
 * Created by Administrator on 2015/12/23.
 */
public final class Program {

    public static void main(String[] args) throws Exception{
        /**
         * 加载log4配置
         */
        PropertyConfigurator.configure(PlatGlobal.instance().getProjectRootPath() + "/sysConfig/log4.properties");

        /**
         * 操作步骤
         *  1.启动創建緩存空間服务，createserver
         *  2.加载项目环境配置
         *  3.加载项目基本配置
         *      sql语句的配置xml
         *      数据库缓存的配置xml
         *  4.启动cacheServer
         *  5.启动codeServer
         *  6.开启rabbitMQ通信连接
         */

        PlatGlobal.instance()
                .preAnalysicConfig("config/queryLogic.xml", QueryLogicConfigModel.class)
                .preAnalysicConfig("config/cacheDbConfig.xml", DbCacheConfigModel.class);

        new CacheServer().run();
        new CodeServer().run();
        new MessageQueueServer().run();
    }
}
