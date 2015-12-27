

package dao.impl;

import dao.BaseDbAccess;
import dao.ConnectionManager;
import dao.po.EDriverType;
import dao.po.SmartStruct;

import java.sql.ResultSet;

/**
 * Created by buns on 12/25/15.
 */
public final class MySqlDbAccess extends BaseDbAccess<SmartStruct> {

    static {
        ConnectionManager.registerDataSource(EDriverType.MYSQL, "mysql-pool.properties");
    }

    public MySqlDbAccess(EDriverType eDriverType) {
        super(eDriverType);
    }

    public MySqlDbAccess(EDriverType eDriverType, int batchSize) {
        super(eDriverType, batchSize);
    }

    @Override
    protected void eachResultSet(ResultSet set) {

    }
}