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
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by buns on 12/27/15.
 * 根据节点创建java文件
 */
public class CreateNodeProcessor extends Processor {

    @Override
    public void doProcess(Map<String, String> params) {

    }

    /**
     * 执行逻辑：
     *  1.加载需要使用的缓存配置
     *  2.判断当前节点所在业务线的xml文件是否存在
     *  3.判断是否存在当前节点的文件夹以及test文件夹是否存在
     *  4.根据节点id查询数据库获取className
     *  5.组织生成java文件所需参数
     *    templateRoot        [模板的根目录]
     *    templateFilePath    [模板文件的地址]
     *    destinationFileName [目标生成文件的地址]
     *    templateParams      [模板解析时候需要的参数字典]
     *  6.生成java文件
     * @param param
     */

    @Override
    public void doProcess(String param) {
        SmartStruct smartStruct;
        File nodePath;
        File testPath;
        String test_node;
        String path;

        String templateName="busiNodeMain.ftl";

        Map<String,Object> root = new HashMap<>();
        root.put("creater","zandezhang");
        root.put("createTime","");
        root.put("isEmpty",false);
        root.put("packageName","com.aa");
        root.put("baseNodePackageName","com.bb");

        String templatePath=PlatGlobal.instance().getProjectRootPath()+"\\src\\config\\codeTemplate\\java";

        Properties properties = PlatGlobal.instance().getPlatformConfig();
        String baseRoot = properties.getProperty("baseRoot");

        QueryLogicConfigModel model = PlatGlobal.instance().getCacheModels(QueryLogicConfigModel.class);
        String node_sql = model.find("code:node-query").getCmd();

        String[] app = param.split(":");
        path=baseRoot+"\\app_" + app[0] + "\\line_" + app[1];
        /**
         * 当客户端传了appid,lineid和nodeid
         */
        String[] t_node = app[2].split(",");
        test_node = path + "\\node_";
        File file = new File(path + "\\line_" + app[1] + ".xml");
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
        for (int l = 0; l < t_node.length; l++) {
            nodePath = new File(test_node+t_node[l] + "\\template");
            testPath = new File(test_node+t_node[l] + "\\test");

            if (!nodePath.exists()) {
                nodePath.mkdirs();
            }
            if (!testPath.exists()) {
                testPath.mkdirs();
            }

            smartStruct = MySqlDbAccess.getInstance().query(node_sql,
                    new DBParam[]{new DBParam(EParamType.STRING, t_node[l])},
                    null);

            root.put("className",smartStruct.getRows().get(0)[0]);
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
