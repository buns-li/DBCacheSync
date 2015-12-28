

package dao.impl;

import dao.BaseDbAccess;
import dao.ConnectionManager;
import dao.po.EDriverType;
import dao.po.SmartStruct;


/**
 * Created by buns on 12/25/15.
 */
public final class MySqlDbAccess extends BaseDbAccess<SmartStruct> {

    static {
        ConnectionManager.registerDataSource(EDriverType.MYSQL, "mysql-pool.properties");
    }
    private MySqlDbAccess() {
        super(EDriverType.MYSQL);
    }

    public static MySqlDbAccess getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        public final static MySqlDbAccess instance = new MySqlDbAccess();
    }

}