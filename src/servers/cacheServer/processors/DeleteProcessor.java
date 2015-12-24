package servers.cacheServer.processors;

import core.Processor;
import redis.clients.jedis.Pipeline;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class DeleteProcessor extends Processor {

    private Pipeline _pipeline;

    public DeleteProcessor(Pipeline pipeline){
        super();
        _pipeline = pipeline;
    }

    @Override
    public void doProcess(Map<String, String> params) {

    }

    @Override
    public void doProcess(String param) {
        System.out.println(param);
    }

    @Override
    public void doProcess(Object[] param) {

    }
}
