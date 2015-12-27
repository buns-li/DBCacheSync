package dao.converter.impl;

import dao.converter.IResultSetConverter;
import dao.po.SmartStruct;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Created by buns on 12/27/15.
 * <p/>
 * 将ResultSet转换成SmartStruct
 */
public class SmartStructConverter implements IResultSetConverter<SmartStruct> {

    @Override
    public SmartStruct convert(ResultSet resultSet) {

        ResultSetMetaData metaData = null;
        try {
            metaData = resultSet.getMetaData();

            Integer columnsCount = metaData.getColumnCount();

            int i = 0;

            SmartStruct smartStruct = new SmartStruct();

            Map<String, Integer> cols = smartStruct.getCols();

            List<String[]> rows = smartStruct.getRows();

            for (; i < columnsCount; i++) {
                cols.put(metaData.getColumnName(i + 1), i);
            }

            while (resultSet.next()) {
                String[] row = new String[columnsCount];

                for (i = 1; i <= columnsCount; i++) {
                    switch (metaData.getColumnType(i)) {
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.LONGNVARCHAR:
                            row[i - 1] = resultSet.getNString(i);
                            break;
                        case Types.CHAR:
                        case Types.LONGVARCHAR:
                        case Types.VARCHAR:
                            row[i - 1] = resultSet.getString(i);
                            break;
                        case Types.BIT:
                            row[i - 1] = resultSet.getBoolean(i) ? "1" : "0";
                            break;
                        case Types.INTEGER:
                            row[i - 1] = String.valueOf(resultSet.getInt(i));
                            break;
                        case Types.BIGINT:
                            row[i - 1] = String.valueOf(resultSet.getLong(i));
                            break;
                        case Types.BOOLEAN:
                            row[i - 1] = String.valueOf(resultSet.getBoolean(i));
                            break;
                        case Types.DATE:
                            row[i - 1] = String.valueOf(resultSet.getDate(i));
                            break;
                        case Types.TIME:
                            row[i - 1] = String.valueOf(resultSet.getTime(i));
                            break;
                        case Types.TIMESTAMP:
                            row[i - 1] = String.valueOf(resultSet.getTimestamp(i));
                            break;
                        default:
                            row[i - 1] = "";
                            break;
                    }
                }
                rows.add(row);
            }
            return smartStruct;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
