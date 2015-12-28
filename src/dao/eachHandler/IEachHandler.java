package dao.eachHandler;

import java.sql.ResultSet;
import java.util.Map;

/**
 * Created by buns on 12/28/15.
 * 专门针对ResultSet行数据进行遍历读取后每一行的操作
 */
public interface IEachHandler {

    /**
     * 遍历每一行的ResultSet数据
     *
     * @param resultSet
     * @param colMap
     */
    void each(ResultSet resultSet, Map<String, Integer> colMap, Integer rowIndex);
}
