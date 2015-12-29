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
     * 执行逻辑：
     *  1.加载需要使用的缓存配置
     *  2.判断当前业务线的xml文件是否存在
     *  3.根据业务线id获取所有节点的相关参数
     *  4.循环
     *  5.判断是否存在当前节点的文件夹以及test文件夹是否存在
     *  6.组织生成java文件所需参数
     *    templateRoot        [模板的根目录]
     *    templateFilePath    [模板文件的地址]
     *    destinationFileName [目标生成文件的地址]
     *    templateParams      [模板解析时候需要的参数字典]
     *  7.生成java文件
     * @param param
     */
    @Override
    public void doProcess(String param) {
        //数据库操作类
        SmartStruct smartStruct;

        File nodePath;
        File testPath;
        String nodeid;
        String test_node;
        String path;
        String className;


        //项目的跟目录
        String rootPath=PlatGlobal.instance().getProjectRootPath();

        String templatePath= rootPath+"\\src\\config\\codeTemplate\\java";

        //模板文件名
        String templateName="busiNodeMain.ftl";
        /**
         * 模板生成文件所需参数
        */
        Map<String,Object> root = new HashMap<>();
        root.put("creator","zandezhang");
        root.put("createTime","");
        root.put("isEmpty",false);
        root.put("packageName","com.aa");
        root.put("baseNodePackageName","com.bb");

        //文件生成地址所在根目录
        Properties properties = PlatGlobal.instance().getPlatformConfig();
        String baseRoot = properties.getProperty("baseRoot");

        //从缓存中获取需要操作的sql
        QueryLogicConfigModel model = PlatGlobal.instance().getCacheModels(QueryLogicConfigModel.class);
        String line_sql = model.find("code:line-query").getCmd();

        String[] app = param.split(":");
        String[] line = app[1].split(",");

        for(int i=0;i<line.length;i++){
            path=baseRoot+"\\app_" + app[0] + "\\line_" + line[i];
            test_node = path + "\\node_";
            File file = new File(path + "\\line_" + line[i] + ".xml");
            /**
             * 判断生成当前业务线的xml配置文件是否存在，
             * 存在则不再创建xml文件，只创建节点文件，
             * 否则同时创建xml与节点文件
             */
            if(!file.exists()){
                //创建xml
                //CreateLineXml.WriteXml(app[0], app[1], fileSrc);
            }
            /**
             * 创建节点文件
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
                    FreeMarkerUtils.createFile(templatePath, templateName, nodePath+"\\"+className+".java", root);
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
