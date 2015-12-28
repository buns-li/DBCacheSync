package dao.eachHandler.impl;

import dao.eachHandler.IEachHandler;

import java.sql.ResultSet;

/**
 * Created by buns on 12/28/15.
 */
public class DefaultEachHandler implements IEachHandler {

    private DefaultEachHandler() {
    }

    public static DefaultEachHandler getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 遍历每一行的ResultSet数据
     *
     * @param resultSet
     */
    @Override
    public void each(ResultSet resultSet) {

    }

    private static class SingletonHolder {
        public final static DefaultEachHandler instance = new DefaultEachHandler();
    }
}
