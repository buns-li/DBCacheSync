package dbAccess;

import java.util.Map;

/**
 * Created by buns on 12/25/15.
 */
public abstract class BaseDbAccess<T> implements IDbAccess<T> {

    private String conUrl;
    private String userName;
    private String password;

    protected BaseDbAccess(String conUrl, String userName, String password) {
        this.conUrl = conUrl;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public T query(String sql, Object... dbParams) {
        return null;
    }

    @Override
    public int execSql(String sql, Object... dbParams) {

        return -1;
    }

    @Override
    public int execSqls(Map<String, Object[]> sqls) {
        return -1;
    }
}
