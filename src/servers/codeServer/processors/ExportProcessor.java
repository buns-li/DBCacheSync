package servers.codeServer.processors;

import core.PlatGlobal;
import core.Processor;
import dao.impl.MySqlDbAccess;
import dao.po.DBParam;
import dao.po.EParamType;
import dao.po.SmartStruct;
import models.QueryLogicConfigModel;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by buns on 12/27/15.
 * 1.将有关文件复制到当前应用文件夹下的同上文件夹中
 * 2.压缩文件夹
 */
public class ExportProcessor extends Processor {
    static InputStream inStream ;
    static FileOutputStream fs;
    @Override
    public void doProcess(Map<String, String> params) {

    }

    @Override
    public void doProcess(String param) {
        //数据库操作类
        SmartStruct smartStruct;

        String nodeid;
        String lineid;
        String oldPath;
        String newPath;

        //获取文件保存根目录
        Properties properties = PlatGlobal.instance().getPlatformConfig();
        String baseRoot = properties.getProperty("baseRoot");
        //复制公共文件
        copyPublicfolder(baseRoot,param);

        //复制节点文件
        QueryLogicConfigModel model = PlatGlobal.instance().getCacheModels(QueryLogicConfigModel.class);
        String app_sql = model.find("code:app-query").getCmd();
        smartStruct = MySqlDbAccess.getInstance().query(app_sql,
                new DBParam[]{new DBParam(EParamType.STRING, param)},
                null);
        String path=baseRoot+"\\app_"+param+"\\app_"+param;
        for(int i=0;i<smartStruct.getRows().size();i++){
            nodeid = smartStruct.getRows().get(i)[1];
            lineid = smartStruct.getRows().get(i)[0];
            oldPath=baseRoot+"\\app_"+param+"\\line_"+lineid+"\\node_"+nodeid+"\\success";
            newPath=baseRoot+"\\app_"+param+"\\app_"+param+"\\line_"+lineid+"\\node_"+nodeid+"";

            getFiles(oldPath, newPath);
        }
        //压缩文件
        zipOutput(path);

    }

    @Override
    public void doProcess(Object[] param) {

    }

    /**
     * 将公共的东西复制到当前应用文件夹下的同名文件夹中
     * @param fireSrc 文件根目录
     * @param appid
     */
    public final static void copyPublicfolder(String fireSrc,String appid) {
        String[] pu = new String[]{"lib","base"};

        for (int i=0;i<pu.length;i++){
            String java_file=fireSrc+"\\"+pu[i];

            String zip_path = fireSrc+"\\app_"+appid+"\\app_"+appid+"\\"+pu[i];

            getFiles(java_file,zip_path);
        }

    }

    /**
     * 获取目录下所有文件
     * @param oldPath 文件所在地址
     * @param newPath 文件要移动到的地址
     */
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

    /**
     * 移动文件
     * @param fileName 要移动的文件名
     * @param newPath 文件要移动到的地址
     */
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
        }

    }

    /**
     * 将文件打成压缩包
     * @param zip_Path 要压缩文件夹所在路径
     */
    public final static void zipOutput(String zip_Path){

        File zipath = new File(zip_Path);

        if(!zipath.exists()){
            zipath.mkdirs();
        }

        //压缩包所在地址与名称
        File zipFile = new File(zip_Path+".zip");
        //要压缩的文件夹
        File srcdir = new File(zip_Path);

        if(!srcdir.exists()){
            throw new RuntimeException(zip_Path+"不存在！");
        }

        Project prj = new Project();

        //整个打包
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        zip.addFileset(fileSet);
        zip.execute();

    }
}
