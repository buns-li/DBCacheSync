package utils;

import dao.impl.MySqlDbAccess;
import dao.po.DBParam;
import dao.po.EDriverType;
import dao.po.EParamType;
import dao.po.SmartStruct;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by buns on 12/27/15.
 */
public class mysqlAccessTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {

    }

    public void testDoResultSetRowConvert() throws Exception {

    }

    public void testQuery() throws Exception {
        MySqlDbAccess mySqlAccess = new MySqlDbAccess(EDriverType.MYSQL);

        String sql = "select id,name,desp from t_busi_app where id = ?";

        SmartStruct s = mySqlAccess.query(sql, new DBParam[]{
                new DBParam(EParamType.STRING, "1")
        });

        System.out.println(s);

        for (Iterator iterator = s.getCols().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();

            System.out.println("key:" + entry.getKey());
            System.out.println("value:" + entry.getValue());
        }

        for (String[] arr : s.getRows()) {
            System.out.printf(arr[0] + arr[1] + arr[2]);
        }
    }

    public void testExec() throws Exception {
        MySqlDbAccess mySqlAccess = new MySqlDbAccess(EDriverType.MYSQL);

        String sql = "update t_busi_app set name = ? where id = ?";

        int i = mySqlAccess.exec(sql, new DBParam[]{
                new DBParam(EParamType.STRING, "app"),
                new DBParam(EParamType.STRING, "1")
        });

        System.out.println(i);
    }

    public void testBatchExec() throws Exception {
        MySqlDbAccess mySqlAccess = new MySqlDbAccess(EDriverType.MYSQL);

        Map<String, DBParam[]> maps = new HashMap<>();

        maps.put("update t_busi_app set name = ? where id = ?", new DBParam[]{
                new DBParam(EParamType.STRING, "app"),
                new DBParam(EParamType.STRING, "1")
        });

        maps.put("update t_busi_app set desp = ? where id = ?", new DBParam[]{
                new DBParam(EParamType.STRING, "appDesp"),
                new DBParam(EParamType.STRING, "1")
        });

        int i = mySqlAccess.batchExec(maps);

        System.out.println(i);
    }

    public void testRepeatBatchExec() throws Exception {
        MySqlDbAccess mySqlAccess = new MySqlDbAccess(EDriverType.MYSQL);

        String sql = "update t_busi_app set name = ? where id = ?";

        ArrayList<DBParam[]> listDBParams = new ArrayList<>();

        listDBParams.add(new DBParam[]{
                new DBParam(EParamType.STRING, "app"),
                new DBParam(EParamType.STRING, "1")
        });

        listDBParams.add(new DBParam[]{
                new DBParam(EParamType.STRING, "app2"),
                new DBParam(EParamType.STRING, "1")
        });

        int i = mySqlAccess.repeatBatchExec(sql, listDBParams);

        System.out.println(i);
    }
}