import servers.cacheServer.CacheServer;
import servers.codeServer.CodeServer;
import core.PlatGlobal;
import servers.mqServer.MessageQueueServer;

/**
 * Created by Administrator on 2015/12/23.
 */
public final class Program {

    public static void main(String[] args) throws Exception{
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
                .preAnalysicConfig("config/queryLogic.xml")
                .preAnalysicConfig("config/cacheDbConfig.xml");

        new CacheServer().run();
        new CodeServer().run();
        new MessageQueueServer().run();
    }
}
