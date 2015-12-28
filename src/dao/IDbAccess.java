package dao;

import dao.converter.IResultSetConverter;
import dao.po.DBParam;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by buns on 12/25/15.
 */
public interface IDbAccess<T> {

    /**
     * 执行数据查询
     * @param sql
     * @param dbParams
     * @return
     */
    ResultSet query(String sql, DBParam[] dbParams);

    /**
     * 执行数据查询(数据结果可以自定义实现转换)
     * @param sql
     * @param dbParamses
     * @param resultSetConverter
     * @return
     */
    T query(String sql, DBParam[] dbParamses, IResultSetConverter<T> resultSetConverter);

    /**
     * 执行数据操作
     * 新增、修改、删除
     *
     * @param sql
     * @param dbParamses
     * @return
     */
    int exec(String sql, DBParam[] dbParamses);

    /**
     * 批量执行多个不同表不同操作的数据操作
     * 新增、修改、删除
     *
     * @param sqls
     * @return
     */
    int batchExec(Map<String, DBParam[]> sqls);

    /**
     * 批量执行单表的新增、修改、删除
     *
     * @param sql
     * @param listDbParams
     * @return
     */
    int repeatBatchExec(String sql, ArrayList<DBParam[]> listDbParams);
}
