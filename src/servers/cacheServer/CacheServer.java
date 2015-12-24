package servers.cacheServer;

import core.ServerBase;
import core.PlatGlobal;
import servers.mqServer.MessageQueueServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import servers.cacheServer.processors.*;

import java.util.Properties;

/**
 * Created by Administrator on 2015/12/23.
 *
 * 缓存服务
 *  1.默认开启redis连接
 *
 */
public class CacheServer extends ServerBase {

    private static Pipeline redisPipeline;

    static {

        Properties configs = PlatGlobal.instance().getPlatformConfig();

        Jedis jedis = new Jedis(configs.get("redis_host").toString(),Integer.parseInt(configs.get("redis_port").toString()));

        redisPipeline = jedis.pipelined();
    }

    @Override
    public void run() {
        System.out.println("CacheServer begin running...");
        System.out.println("CacheServer begin registering the routeKey processor in MessageQueueServer！");
        MessageQueueServer.registerRoute("cache.insert", new InsertProcessor(redisPipeline));
        MessageQueueServer.registerRoute("cache.update", new UpdateProcessor(redisPipeline));
        MessageQueueServer.registerRoute("cache.delete", new DeleteProcessor(redisPipeline));
        System.out.println("CacheServer Complete registering the routeKey processor in MessageQueueServer！\n");

        //获取数据库数据并存入缓存
    }
}
