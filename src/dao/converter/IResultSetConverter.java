package dao.converter;

import java.sql.ResultSet;

/**
 * Created by buns on 12/27/15.
 */
public interface IResultSetConverter<T> {
    /**
     * 将ResultSet转换成对应的数据类型
     *
     * @param resultSet
     * @return
     */
    T convert(ResultSet resultSet);
}
