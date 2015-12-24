package models.po;

import core.POBase;

import java.util.LinkedList;

/**
 * Created by buns on 12/24/15.
 */
public class DbCacheTablePO extends POBase {
    /**
     * 表标识
     */
    private String key;
    /**
     * 表名称
     */
    private String name;
    /**
     * 统一获取数据的查询sqlKey
     */
    private String sqlKey;
    /**
     * 数据发生更新好执行的sql语句对应的key
     */
    private String getNewSqlKey;

    private LinkedList<DbCacheTableColPO> cols;

    public DbCacheTablePO(String key, String name, String sqlKey, String getNewSqlKey) {
        this.key = key;
        this.name = name;
        this.sqlKey = sqlKey;
        this.getNewSqlKey = getNewSqlKey;
        cols = new LinkedList<>();
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSqlKey() {
        return sqlKey;
    }

    public String getGetNewSqlKey() {
        return getNewSqlKey;
    }

    public LinkedList<DbCacheTableColPO> getCols() {
        return cols;
    }

}
