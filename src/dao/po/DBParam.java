package dao.po;

/**
 * Created by buns on 12/27/15.
 */
public final class DBParam {
    private EParamType paramType;
    private Object value;

    public DBParam(EParamType paramType, Object value) {
        this.paramType = paramType;
        this.value = value;
    }

    final public EParamType getParamType() {
        return paramType;
    }

    final public Object getValue() {
        return value;
    }
}
