package dao;

import dao.po.DBParams;
import dao.po.DIO;
import dao.po.EDriverType;
import dao.po.SmartStruct;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

/**
 * Created by buns on 12/25/15.
 */
public abstract class BaseDbAccess implements IDbAccess {

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

    private static void setValueToPreparedStatement(PreparedStatement ps, Integer index, DBParams param) {
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

    public abstract <T extends SmartStruct & ResultSet> T doResultSetRowConvert(ResultSet set);

    @Override
    public <T extends SmartStruct & ResultSet> T query(DIO dio) {
        connection = ConnectionManager.getConnection(eDriverType);
        PreparedStatement preparedStatement = null;
        ResultSet set = null;
        try {
            preparedStatement = connection.prepareStatement(dio.getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            DBParams[] params = dio.getParams();
            for (int i = 0, l = params.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i, params[i]);
            }
            set = preparedStatement.executeQuery();
            return doResultSetRowConvert(set);
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
            ConnectionManager.close(eDriverType);
        }
    }

    @Override
    public int exec(DIO dio) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(dio.getSql());
            preparedStatement.setFetchSize(Integer.MIN_VALUE);
            DBParams[] params = dio.getParams();
            for (int i = 0, l = params.length; i < l; i++) {
                setValueToPreparedStatement(preparedStatement, i, params[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionManager.close(eDriverType);
        }
        return -1;
    }

    @Override
    public int batchExec(DIO... dios) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            PreparedStatement ps;
            DBParams[] params;

            connection.setAutoCommit(false);

            for (int i = 0, l = dios.length, j, l2; i < l; i++) {
                ps = connection.prepareStatement(dios[i].getSql());
                params = dios[i].getParams();
                for (j = 0, l2 = params.length; j < l2; j++) {
                    setValueToPreparedStatement(ps, j, params[j]);
                }
                ps.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            ConnectionManager.rollback(eDriverType, null);
            e.printStackTrace();
        } finally {
            ConnectionManager.close(eDriverType);
        }
        return -1;
    }

    @Override
    public int repeatBatchExec(DIO dio) {
        connection = ConnectionManager.getConnection(eDriverType);
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(dio.getSql());

            List<DBParams[]> dbParamses = dio.getListParams();

            int i = 0, l = dbParamses.size(), j, l2;
            DBParams[] dbParams;

            for (; i < l; i++) {
                dbParams = dbParamses.get(i);
                for (j = 0, l2 = dbParams.length; j < l2; j++) {
                    setValueToPreparedStatement(ps, i, dbParams[j]);
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
        } catch (SQLException e) {
            ConnectionManager.rollback(eDriverType, null);
            e.printStackTrace();
        } finally {
            ConnectionManager.close(eDriverType);
        }
        return -1;
    }
}
