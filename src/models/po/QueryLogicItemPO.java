package models.po;

import core.POBase;

/**
 * Created by buns on 12/24/15.
 */
public class QueryLogicItemPO extends POBase {

    private String key;
    private String cmd;

    public QueryLogicItemPO() {
    }

    public QueryLogicItemPO(String key, String cmd) {
        this.key = key;
        this.cmd = cmd;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
