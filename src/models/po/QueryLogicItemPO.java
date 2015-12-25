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

    final public String getKey() {
        return key;
    }

    final public void setKey(String key) {
        this.key = key;
    }

    final public String getCmd() {
        return cmd;
    }
}
