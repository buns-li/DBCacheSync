package po;

import java.util.Map;

/**
 * Created by Administrator on 2015/12/23.
 */
public class TablePO {

    private String tableName;
    private String tableNo;
    private Map<String, ColPO> cols;
    private Map<String, SqlPo> sqls;
    private Map<String, CountPO> ccs;
    private Map<String, FkPo> fkc;

    public TablePO() {
        super();
    }
    public TablePO(String tableName, String tableNo, Map<String, ColPO> cols, Map<String, SqlPo> sqls, Map<String, CountPO> ccs, Map<String, FkPo> fkc) {
        this.tableName = tableName;
        this.tableNo = tableNo;
        this.cols = cols;
        this.sqls = sqls;
        this.ccs = ccs;
        this.fkc = fkc;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public Map<String, ColPO> getCols() {
        return cols;
    }

    public void setCols(Map<String, ColPO> cols) {
        this.cols = cols;
    }

    public Map<String, SqlPo> getSqls() {
        return sqls;
    }

    public void setSqls(Map<String, SqlPo> sqls) {
        this.sqls = sqls;
    }

    public Map<String, CountPO> getCcs() {
        return ccs;
    }

    public void setCcs(Map<String, CountPO> ccs) {
        this.ccs = ccs;
    }

    public Map<String, FkPo> getFkc() {
        return fkc;
    }

    public void setFkc(Map<String, FkPo> fkc) {
        this.fkc = fkc;
    }
}
