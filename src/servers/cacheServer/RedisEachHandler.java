package servers.cacheServer;

import dao.eachHandler.IEachHandler;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by buns on 12/28/15.
 */
public class RedisEachHandler implements IEachHandler {

    public RedisEachHandler() {

    }

    /**
     * 遍历每一行的ResultSet数据
     *
     * @param resultSet
     * @param colMap
     */
    @Override
    public void each(ResultSet resultSet, Map<String, Integer> colMap, Integer rowIndex) {

    }
}
