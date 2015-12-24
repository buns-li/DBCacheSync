package models;

/**
 * Created by Administrator on 2015/12/1.
 */
public class t_cache_sync {
    private String tableName;
    private String firstTime;
    private String updateTime;
    private int backcount;
    private int frontcount;

    public t_cache_sync(String tableName, String firstTime, String updateTime, int backcount, int frontcount) {
        this.firstTime = firstTime;
        this.tableName = tableName;
        this.updateTime = updateTime;
        this.backcount = backcount;
        this.frontcount = frontcount;
    }

    public t_cache_sync() {
        super();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(String firstTime) {
        this.firstTime = firstTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getFrontcount() {
        return frontcount;
    }

    public void setFrontcount(int frontcount) {
        this.frontcount = frontcount;
    }

    public int getBackcount() {
        return backcount;
    }

    public void setBackcount(int backcount) {
        this.backcount = backcount;
    }
}
