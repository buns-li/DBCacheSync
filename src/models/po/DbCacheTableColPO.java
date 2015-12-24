package models.po;

import core.POBase;

/**
 * Created by buns on 12/24/15.
 */
public class DbCacheTableColPO extends POBase {
    private String name;
    private String type;
    private Integer index;
    private Integer length;
    private String fieldType;
    private String fromTb;

    public DbCacheTableColPO(String name, String type, Integer length, Integer index, String fieldType, String fromTb) {
        this.name = name;
        this.type = type;
        this.index = index;
        this.length = length;
        this.fieldType = fieldType;
        this.fromTb = fromTb;
    }

    public String getFromTb() {
        return fromTb;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getLength() {
        return length;
    }
}
