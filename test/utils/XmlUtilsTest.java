package utils;

import junit.framework.TestCase;
import models.DbCacheConfigModel;
import models.QueryLogicConfigModel;
import models.po.DbCacheTableColPO;

import java.util.LinkedList;

/**
 * Created by buns on 12/24/15.
 */
public class XmlUtilsTest extends TestCase {

    QueryLogicConfigModel queryLogicConfigPO;

    DbCacheConfigModel dbCacheConfigModel;

    String testRootPath = System.getProperty("user.dir") + "/test/";

    //此方法在执行每一个测试方法之前（测试用例）之前调用
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        queryLogicConfigPO = new QueryLogicConfigModel();
        queryLogicConfigPO.addItem("cache:app-query", "select * from t_busi_app;");

        dbCacheConfigModel = new DbCacheConfigModel();

        dbCacheConfigModel.addItem("t1", "t_busi_app", "cache:app-query", "cache:app-update");

        LinkedList<DbCacheTableColPO> cols = dbCacheConfigModel.find("t1").getCols();

        cols.add(new DbCacheTableColPO("id", "string", 12, 0, "pk", ""));
        cols.add(new DbCacheTableColPO("name", "string", 25, 1, "", ""));
        cols.add(new DbCacheTableColPO("pkid", "string", 12, 2, "fk", "t2"));
    }

    //此方法在执行每一个测试方法之后调用
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.out.println("tearDown()");
    }

    public void testSerialize() {
        XmlUtils.serialize(queryLogicConfigPO, testRootPath + "test.xml");

        XmlUtils.serialize(dbCacheConfigModel, testRootPath + "test2.xml");
    }

    public void testDeserialize() {
        QueryLogicConfigModel queryLogicConfigModel = XmlUtils.deserialize(QueryLogicConfigModel.class, testRootPath + "test.xml");

        System.out.println(queryLogicConfigModel.find("cache:app-query").getCmd());

        DbCacheConfigModel dbCacheConfigModel = XmlUtils.deserialize(DbCacheConfigModel.class, testRootPath + "test2.xml");

        System.out.println(dbCacheConfigModel.find("t1").getName());
    }
}
