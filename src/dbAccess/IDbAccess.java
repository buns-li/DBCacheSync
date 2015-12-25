package dbAccess;

import java.util.Map;

/**
 * Created by buns on 12/25/15.
 */
public interface IDbAccess<T> {

    T query(String sql, Object... dbParams);

    int execSql(String sql, Object... dbParams);

    int execSqls(Map<String, Object[]> sqls);
}
