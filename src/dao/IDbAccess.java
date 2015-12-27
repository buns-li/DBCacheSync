package dao;

import dao.po.DIO;
import dao.po.SmartStruct;

import java.sql.ResultSet;

/**
 * Created by buns on 12/25/15.
 */
public interface IDbAccess {
    /**
     * 执行数据查询
     *
     * @param dio
     * @param <T>
     * @return
     */
    <T extends SmartStruct & ResultSet> T query(DIO dio);

    /**
     * 执行数据操作
     * 新增、修改、删除
     *
     * @param dio
     * @return
     */
    int exec(DIO dio);

    /**
     * 批量执行多个不同表不同操作的数据操作
     * 新增、修改、删除
     *
     * @param dios
     * @return
     */
    int batchExec(DIO... dios);

    /**
     * 批量执行单表的新增、修改、删除
     *
     * @param dio
     * @return
     */
    int repeatBatchExec(DIO dio);
}
