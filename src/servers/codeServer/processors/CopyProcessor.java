package servers.codeServer.processors;
import core.PlatGlobal;
import core.Processor;
import dao.impl.MySqlDbAccess;
import dao.po.DBParam;
import dao.po.EParamType;
import dao.po.SmartStruct;
import models.QueryLogicConfigModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/12/23.
 */
public class CopyProcessor extends Processor{
    static InputStream inStream ;
    static FileOutputStream fs;

    @Override
    public void doProcess(Map<String, String> params) {

    }

    @Override
    public void doProcess(String param) {
        System.out.println(param);
    }

    /**
     * 执行逻辑：
     *  1.解析消息，根据解析获取的参数组织路径获取文件
     * 2.copy文件
     */
    @Override
    public void doProcess(Object[] param) {
        //数据库操作类
        SmartStruct smartStruct;

        String nodeid;
        String lineid;
        String line_sql;
        String app_sql;
        Object[] message;
        String[] line_all;
        String[] node_all;
        String oldPath;
        String newPath;
        String baseRoot;

        //获取文件根目录
        Properties properties = PlatGlobal.instance().getPlatformConfig();
        baseRoot = properties.getProperty("baseRoot");

        //从缓存中获取所需的sql
        QueryLogicConfigModel model = PlatGlobal.instance().getCacheModels(QueryLogicConfigModel.class);
        line_sql = model.find("code:line-query").getCmd();
        app_sql = model.find("code:app-query").getCmd();

        /**
         * message是一个数组
         */
        message=param.toString().split(":");
        //当客户端传了appid与lineid与nodeid
        if(message.length==3){
            //获取节点id数组
            node_all = message[2].toString().split(",");
            //循环并复制文件
            for(int i=0;i<node_all.length;i++){
                //oldpath:文件所在路径
                //newPath:文件将要复制到的路径
                oldPath=baseRoot+"\\app_"+message[0]+"\\line_"+message[1]+"\\node_"+node_all[i]+"\\test";
                newPath=baseRoot+"\\app_"+message[0]+"\\line_"+message[1]+"\\node_"+node_all[i]+"\\success";
                getFiles(oldPath,newPath);
            }
        }
        /**
         * 当客户端传了appid与lineid
         */
        if(message.length == 2) {
            //获取业务线id数组
            line_all = message[1].toString().split(",");
            //循环获取业务线上的所有节点
            for(int i=0;i<line_all.length;i++){
                //获取节点id
                smartStruct = MySqlDbAccess.getInstance().query(line_sql,
                        new DBParam[]{new DBParam(EParamType.STRING, line_all[i])},
                        null);
                //循环并复制文件
                for (int j=0;j<smartStruct.getRows().size();j++){
                    nodeid=smartStruct.getRows().get(j)[0];
                    //oldpath:文件所在路径
                    //newPath:文件将要复制到的路径
                    oldPath=baseRoot+"\\app_"+message[0]+"\\line_"+message[1]+"\\node_"+nodeid+"\\test";
                    newPath=baseRoot+"\\app_"+message[0]+"\\line_"+message[1]+"\\node_"+nodeid+"\\success";
                    getFiles(oldPath,newPath);
                }
            }
        }

        /**
         * 当客户端只传了appid
         */

        if(message.length == 1){
            //获取当前业务线下的所有相关节点
            smartStruct = MySqlDbAccess.getInstance().query(app_sql,
                    new DBParam[]{new DBParam(EParamType.STRING, message[0])},
                    null);
            //循环并复制文件
            for (int i=0;i<smartStruct.getRows().size();i++){
                nodeid = smartStruct.getRows().get(i)[1];
                lineid = smartStruct.getRows().get(i)[0];
                //oldpath:文件所在路径
                //newPath:文件将要复制到的路径
                oldPath=baseRoot+"\\app_"+message[0]+"\\line_"+lineid+"\\node_"+nodeid+"\\test";
                newPath=baseRoot+"\\app_"+message[0]+"\\line_"+lineid+"\\node_"+nodeid+"\\success";
                getFiles(oldPath,newPath);
            }

        }


    }
    static void getFiles(String oldPath,String newPath) {
        File root = new File(oldPath);
        //获取当前路径下的所有文件以及子目录
        File[] files = root.listFiles();
        for (File file :files) {
            //是否是文件
            if (file.isFile()) {

                copyFile(file.getAbsolutePath(),newPath);
            }
        }
    }

    static void copyFile(String fileName,String newPath){

        File zipath = new File(newPath);
        if (!zipath.exists()) {
            zipath.mkdirs();
        }
        try{
            int byteread = 0;
            File oldfile = new File(fileName);
            if(oldfile.exists()){
                inStream = new FileInputStream(fileName);
                fs = new FileOutputStream(newPath+"\\"+oldfile.getName());
                byte[] buffer = new byte[1444];

                while((byteread = inStream.read(buffer)) != -1){
                    //文件大小
                    fs.write(buffer,0,byteread);
                }
            }
            else{
                System.out.println("文件不存在");
            }
            inStream.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }

    }
}
