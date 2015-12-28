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
public class CreateLineProcessor extends Processor {
    @Override
    public void doProcess(Map<String, String> params) {

    }

    /**
     * ִ���߼���
     *  1.������Ҫʹ�õĻ�������
     *  2.�жϵ�ǰҵ���ߵ�xml�ļ��Ƿ����
     *  3.ѭ��ҵ����id
     *  4.����ҵ����id��ȡ���нڵ��className
     *  5.ѭ���ڵ�id
     *  6.�ж��Ƿ���ڵ�ǰ�ڵ���ļ����Լ�test�ļ����Ƿ����
     *  7.��֯����java�ļ��������
     *    templateRoot        [ģ��ĸ�Ŀ¼]
     *    templateFilePath    [ģ���ļ��ĵ�ַ]
     *    destinationFileName [Ŀ�������ļ��ĵ�ַ]
     *    templateParams      [ģ�����ʱ����Ҫ�Ĳ����ֵ�]
     *  6.����java�ļ�
     * @param param
     */

    @Override
    public void doProcess(String param) {
        SmartStruct smartStruct;
        File nodePath;
        File testPath;
        String nodeid;
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
        String line_sql = model.find("code:line-query").getCmd();

        String[] app = param.split(":");
        String[] line = app[1].split(",");

        for(int i=0;i<line.length;i++){
            path=baseRoot+"\\app_" + app[0] + "\\line_" + line[i];

            test_node = path + "\\node_";
            File file = new File(path + "\\line_" + line[i] + ".xml");
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
            smartStruct = MySqlDbAccess.getInstance().query(line_sql,
                    new DBParam[]{new DBParam(EParamType.STRING, line[i])},
                    null);
            for (int l = 0; l < smartStruct.getRows().size(); l++) {
                nodeid=smartStruct.getRows().get(l)[0];
                className = smartStruct.getRows().get(l)[1];
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

    }

    @Override
    public void doProcess(Object[] param) {

    }
}
