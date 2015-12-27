package dao.po;

import java.util.ArrayList;

/**
 * Created by buns on 12/26/15.
 */
public final class DIO {

    private String tableName;

    private String sql;

    private DBParam[] params;

    private ArrayList<DBParam[]> listParams;

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

    public DBParam[] getParams() {
        return params;
    }

    public void setParams(DBParam[] params) {
        this.params = params;
    }

    public ArrayList<DBParam[]> getListParams() {
        return listParams;
    }

    public void setListParams(ArrayList<DBParam[]> listParams) {
        this.listParams = listParams;
    }
}
