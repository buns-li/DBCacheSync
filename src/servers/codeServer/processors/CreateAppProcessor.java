package servers.codeServer.processors;

import core.PlatGlobal;
import core.Processor;
import dao.impl.MySqlDbAccess;
import dao.po.DBParam;
import dao.po.EParamType;
import dao.po.SmartStruct;
import models.QueryLogicConfigModel;
import utils.FreeMarkerUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by buns on 12/27/15.
 */
public class CreateAppProcessor extends Processor {
    @Override
    public void doProcess(Map<String, String> params) {

    }

    @Override
    public void doProcess(String param) {

        SmartStruct smartStruct;
        File nodePath;
        File testPath;
        String nodeid;
        String lineid;
        String appid=param;
        String test_node;
        String path;
        String className;

        String templateName="busiNodeMain.ftl";

        Map<String,Object> root = new HashMap<>();
        root.put("creater","zandezhang");
        root.put("createTime","");
        root.put("isEmpty",false);
        root.put("packageName","com.aa");
        root.put("baseNodePackageName","com.bb");

        String templatePath= PlatGlobal.instance().getProjectRootPath()+"\\src\\config\\codeTemplate\\java";

        Properties properties = PlatGlobal.instance().getPlatformConfig();
        String baseRoot = properties.getProperty("baseRoot");

        QueryLogicConfigModel model = PlatGlobal.instance().getCacheModels(QueryLogicConfigModel.class);
        String app_sql = model.find("code:app-query").getCmd();

        smartStruct = MySqlDbAccess.getInstance().query(app_sql,
                new DBParam[]{new DBParam(EParamType.STRING, param)},
                null);

        for(int i=0;i<smartStruct.getRows().size();i++){
            lineid = smartStruct.getRows().get(i)[0];
            nodeid = smartStruct.getRows().get(i)[1];
            className = smartStruct.getRows().get(i)[3];

            path=baseRoot+"\\app_" + param + "\\line_" + lineid;

            test_node = path + "\\node_";
            File file = new File(path + "\\line_" + lineid + ".xml");
            /**
             * �ж����ɵ�ǰҵ���ߵ�xml�����ļ��Ƿ���ڣ�
             * �������ٴ���xml�ļ���ֻ�����ڵ��ļ���
             * ����ͬʱ����xml��ڵ��ļ�
             */
            if(!file.exists()){
                //����xml
                //CreateLineXml.WriteXml(app[0], app[1], fileSrc);
            }
             /**
              * �����ڵ��ļ�
              */
                nodePath = new File(test_node+nodeid + "\\template");
                testPath = new File(test_node+nodeid + "\\test");

                if (!nodePath.exists()) {
                    nodePath.mkdirs();
                }
                if (!testPath.exists()) {
                    testPath.mkdirs();
                }
                root.put("className",className);
                try {
                    FreeMarkerUtils.createFile(templatePath, templateName, nodePath+"\\"+smartStruct.getRows().get(0)[0]+".java", root);
                } catch (IOException e) {
                    e.printStackTrace();
             }

        }

    }

    @Override
    public void doProcess(Object[] param) {

    }
}
