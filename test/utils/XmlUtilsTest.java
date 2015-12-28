package utils;

import junit.framework.TestCase;
import models.BusiLineConfigModel;
import models.DbCacheConfigModel;
import models.QueryLogicConfigModel;
import models.po.BusiLinePO;
import models.po.DbCacheTableColPO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by buns on 12/24/15.
 */
public class XmlUtilsTest extends TestCase {

    QueryLogicConfigModel queryLogicConfigPO;

    DbCacheConfigModel dbCacheConfigModel;

    BusiLineConfigModel busiLineConfigModel;

    String testRootPath = System.getProperty("user.dir") + "/test/";

    //此方法在执行每一个测试方法之前（测试用例）之前调用
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        queryLogicConfigPO = new QueryLogicConfigModel();
        queryLogicConfigPO.addItem("cache:app-query", "select * from t_busi_app;");

        dbCacheConfigModel = new DbCacheConfigModel();

        dbCacheConfigModel.addItem("t1", "t_busi_app", "cache:app-query", "cache:app-update");
        dbCacheConfigModel.addItem("t2", "t_busi_line", "cache:line-query", "cache:line-update");

        LinkedList<DbCacheTableColPO> cols = dbCacheConfigModel.find("t1").getCols();

        cols.add(new DbCacheTableColPO("id", "string", 12, 0, "pk", ""));
        cols.add(new DbCacheTableColPO("name", "string", 25, 1, "", ""));
        cols.add(new DbCacheTableColPO("pkid", "string", 12, 2, "fk", "t2"));

        cols = dbCacheConfigModel.find("t2").getCols();

        cols.add(new DbCacheTableColPO("id", "string", 12, 0, "pk", ""));
        cols.add(new DbCacheTableColPO("name", "string", 25, 1, "", ""));
        cols.add(new DbCacheTableColPO("pkid", "string", 12, 2, "fk", "t1"));


        busiLineConfigModel = new BusiLineConfigModel();

        busiLineConfigModel.id = "line_1";
        busiLineConfigModel.clientId = "clientid_1";
        busiLineConfigModel.root = "node_1";
        busiLineConfigModel.end = "node_2";

        Map<String, BusiLinePO.BusiLineParamPO> params = busiLineConfigModel.getParams();

        params.put("username", new BusiLinePO.BusiLineParamPO("username", "string", "node_1", "node_2"));
        params.put("password", new BusiLinePO.BusiLineParamPO("password", "string", "node_1", "node_2"));

        Map<String, BusiLinePO.BusiNodePO> nodes = busiLineConfigModel.getNodes();

        BusiLinePO.BusiNodePO nodePo = new BusiLinePO.BusiNodePO("node_1", "testNode", "node_2", null);
        nodePo.sqlkeys = new ArrayList<>();

        BusiLinePO.NodeSqlKeyPO sqlKeyPO = new BusiLinePO.NodeSqlKeyPO("app-query");
        sqlKeyPO.getParams().add(new BusiLinePO.BusiLineParamPO("username", "string", null, null));
        sqlKeyPO.getParams().add(new BusiLinePO.BusiLineParamPO("password", "string", null, null));
        nodePo.sqlkeys.add(sqlKeyPO);
        sqlKeyPO = new BusiLinePO.NodeSqlKeyPO("app-login");
        sqlKeyPO.getParams().add(new BusiLinePO.BusiLineParamPO("username", "string", null, null));
        sqlKeyPO.getParams().add(new BusiLinePO.BusiLineParamPO("password", "string", null, null));

        nodePo.sqlkeys.add(sqlKeyPO);

        nodes.put("node_1", nodePo);

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

        XmlUtils.serialize(busiLineConfigModel, testRootPath + "test3.xml");
    }

    public void testDeserialize() {
        QueryLogicConfigModel queryLogicConfigModel = XmlUtils.deserialize(QueryLogicConfigModel.class, testRootPath + "test.xml");

        System.out.println(queryLogicConfigModel.find("cache:app-query").getCmd());

        DbCacheConfigModel dbCacheConfigModel = XmlUtils.deserialize(DbCacheConfigModel.class, testRootPath + "test2.xml");

        System.out.println(dbCacheConfigModel.find("t2").getName());

        BusiLineConfigModel busiLineConfigModel = XmlUtils.deserialize(BusiLineConfigModel.class, testRootPath + "test3.xml");

        for (Iterator iterator = busiLineConfigModel.getParams().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, BusiLinePO.BusiLineParamPO> entry = (Map.Entry<String, BusiLinePO.BusiLineParamPO>) iterator.next();

            System.out.println(entry.getValue().getName());
        }

        for (Iterator iterator = busiLineConfigModel.getNodes().entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, BusiLinePO.BusiNodePO> entry = (Map.Entry<String, BusiLinePO.BusiNodePO>) iterator.next();

            System.out.println(entry.getValue().getId());
        }

    }
}
