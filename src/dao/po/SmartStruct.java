package dao.po;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by buns on 12/27/15.
 */
public final class SmartStruct {
    private Map<String, Integer> cols;

    private List<String[]> rows;

    public SmartStruct() {
        cols = new HashMap<String, Integer>();
        rows = new LinkedList<String[]>();
    }

    final public Map<String, Integer> getCols() {
        return cols;
    }

    final public List<String[]> getRows() {
        return rows;
    }
}
