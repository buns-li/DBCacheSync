package dao.po;

import com.sun.tools.javac.util.List;

/**
 * Created by buns on 12/26/15.
 */
public final class DIO {

    private String tableName;

    private String sql;

    private DBParams[] params;

    private List<DBParams[]> listParams;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public DBParams[] getParams() {
        return params;
    }

    public void setParams(DBParams[] params) {
        this.params = params;
    }

    public List<DBParams[]> getListParams() {
        return listParams;
    }

    public void setListParams(List<DBParams[]> listParams) {
        this.listParams = listParams;
    }
}
