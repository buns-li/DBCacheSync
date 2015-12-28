package dao;

import dao.converter.IResultSetConverter;
import dao.converter.impl.SmartStructConverter;
import dao.eachHandler.IEachHandler;
import dao.eachHandler.impl.DefaultEachHandler;
import dao.po.DBParam;
import dao.po.EDriverType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by buns on 12/25/15.
 */
public abstract class BaseDbAccess<T> implements IDbAccess<T> {

    private static IResultSetConverter DEFAULT_CONVERTER = SmartStructConverter.getInstance();
    private static IEachHandler DEFAULT_EACHHANDLER = DefaultEachHandler.getInstance();

    protected Connection connection;
    private EDriverType eDriverType;
    private int batchSize;

    protected BaseDbAccess(EDriverType eDriverType) {
        this(eDriverType, 1000);
    }

    protected BaseDbAccess(EDriverType eDriverType, int batchSize) {
        this.eDriverType = eDriverType;
        this.batchSize = batchSize;
    }

    /**
     * 预编译语句的参数赋值
     *
     * @param ps
     * @param index
     * @param param
     */
    private static void setValueToPreparedStatement(PreparedStatement ps, Integer index, DBParam param) {
        try {
            Object value = param.getValue();
            switch (param.getParamType()) {
                case STRING:
                    if (value == null) {
                        ps.setNull(index, Types.NVARCHAR);
                    } else {
                        ps.setString(index, value.toString());
                    }
                    break;
                case BIT:
                    if (value == null) {
                        ps.setNull(index, Types.BIT);
                    } else {
                        ps.setBoolean(index, Boolean.parseBoolean(value.toString()));
                    }
                    break;
                case INT:
                    if (value == null) {
                        ps.setNull(index, Types.INTEGER);
                    } else {
                        ps.setInt(index, Integer.parseInt(value.toString()));
                    }
                    break;
                case LONG:
                    if (value == null) {
                        ps.setNull(index, Types.BIGINT);
                    } else {
                        ps.setLong(index, Long.parseLong(value.toString()));
                    }
                    break;
                case FLOAT:
                    if (value == null) {
                        ps.setNull(index, Types.FLOAT);
                    } else {
                        ps.setFloat(index, Float.parseFloat(value.toString()));
                    }
                    break;
                case DOUBLE:
                    if (value == null) {
                        ps.setNull(index, Types.DOUBLE);
                    } else {
                        ps.setDouble(index, Double.parseDouble(value.toString()));
                    }
                    break;
                case DECIMAL:
                    if (value == null) {
                        ps.setNull(index, Types.DECIMAL);
                    } else {
                        ps.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(value.toString())));
                    }
                    break;
                case LONGTEXT:
                    if (value == null) {
                        ps.setNull(index, Types.LONGNVARCHAR);
                    } else {
                        ps.setNString(index, value.toString());
                    }
                case DATE:
                    if (value == null) {
                        ps.setNull(index, Types.DATE);
                    } else {
                        ps.setString(index, value.toString());
                    }
                    break;
                case TIMESTAMP:
                    if (value == null) {
                        ps.setNull(index, Types.TIMESTAMP);
                    } else {
                        ps.setTimestamp(index, Timestamp.valueOf(value.toString()));
                    }
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行数据查询
     *
     * @param sql
     * @param dbParams
     * @return
     */
    @Override
    public ResultSet query(String sql, DBParam[] dbParams) {
        connection = ConnectionManager.getConnection(eDriverType);
        PreparedStatement preparedStatement = null;
        ResultSet set = null;
        try {
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            for (int i = 0, l = dbParams.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i + 1, dbParams[i]);
            }
            set = preparedStatement.executeQuery();
            return set;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionManager.close(connection);
        }
    }

    /**
     * 执行数据查询
     * @param sql
     * @param dbParams
     * @return
     */
    @Override
    public T query(String sql, DBParam[] dbParams, IResultSetConverter<T> resultSetConverter) {
        connection = ConnectionManager.getConnection(eDriverType);
        PreparedStatement preparedStatement = null;
        ResultSet set = null;
        if (resultSetConverter == null) resultSetConverter = DEFAULT_CONVERTER;
        try {
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            for (int i = 0, l = dbParams.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i + 1, dbParams[i]);
            }
            set = preparedStatement.executeQuery();
            return resultSetConverter.convert(set);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionManager.close(connection);
        }
    }

    /**
     * 将查询出来的ResultSet进行行遍历读取
     *
     * @param sql
     * @param dbParams
     */
    public void eachQuery(String sql, DBParam[] dbParams, IEachHandler eachHandler) {
        connection = ConnectionManager.getConnection(eDriverType);
        PreparedStatement preparedStatement = null;
        ResultSet set = null;
        if (eachHandler == null) {
            eachHandler = DEFAULT_EACHHANDLER;
        }
        try {
            preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            for (int i = 0, l = dbParams.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i + 1, dbParams[i]);
            }
            set = preparedStatement.executeQuery();
            while (set.next()) {
                eachHandler.each(set);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (set != null) {
                    set.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionManager.close(connection);
        }
    }

    /**
     * 执行数据操作
     * 新增、修改、删除
     *
     * @param sql
     * @param dbParams
     * @return
     */
    @Override
    public int exec(String sql, DBParam[] dbParams) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0, l = dbParams.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i + 1, dbParams[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * 批量执行多个不同表不同操作的数据操作
     * 新增、修改、删除
     *
     * @param sqls
     * @return
     */
    @Override
    public int batchExec(Map<String, DBParam[]> sqls) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            PreparedStatement ps;
            DBParam[] params;
            connection.setAutoCommit(false);
            int j, l2;
            Map.Entry<String, DBParam[]> dio;
            for (Iterator iterator = sqls.entrySet().iterator(); iterator.hasNext(); ) {
                dio = (Map.Entry<String, DBParam[]>) iterator.next();
                ps = connection.prepareStatement(dio.getKey());
                params = dio.getValue();
                for (j = 0, l2 = params.length; j < l2; j++) {
                    setValueToPreparedStatement(ps, j + 1, params[j]);
                }
                ps.executeUpdate();
            }
            connection.commit();
            return sqls.size();
        } catch (SQLException e) {
            ConnectionManager.rollback(connection, null);
            e.printStackTrace();
            return -1;
        } finally {
            ConnectionManager.close(connection);
        }
    }

    /**
     * 批量执行单表的新增、修改、删除
     *
     * @param sql
     * @param listDbParams
     * @return
     */
    @Override
    public int repeatBatchExec(String sql, ArrayList<DBParam[]> listDbParams) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);

            int i = 0, l = listDbParams.size(), j, l2;
            DBParam[] dbParams;

            for (; i < l; i++) {
                dbParams = listDbParams.get(i);
                for (j = 0, l2 = dbParams.length; j < l2; j++) {
                    setValueToPreparedStatement(ps, j + 1, dbParams[j]);
                }
                ps.addBatch();
                if ((i + 1) % batchSize == 0) {
                    ps.executeBatch();
                    connection.commit();
                }
            }
            if (l % batchSize != 0) {
                ps.executeBatch();
                connection.commit();
            }
            return 1;
        } catch (SQLException e) {
            ConnectionManager.rollback(connection, null);
            e.printStackTrace();
            return -1;
        } finally {
            ConnectionManager.close(connection);
        }
    }
}
