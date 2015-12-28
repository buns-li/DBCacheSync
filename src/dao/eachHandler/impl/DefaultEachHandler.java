package dao.eachHandler.impl;

import dao.eachHandler.IEachHandler;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by buns on 12/28/15.
 */
public final class DefaultEachHandler implements IEachHandler {

    private DefaultEachHandler() {
    }

    public static DefaultEachHandler getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 遍历每一行的ResultSet数据
     *
     * @param resultSet
     * @param colMap
     * @param rowIndex
     */
    @Override
    public void each(ResultSet resultSet, Map<String, Integer> colMap, Integer rowIndex) {

    }

    private static class SingletonHolder {
        public final static DefaultEachHandler instance = new DefaultEachHandler();
    }
}
