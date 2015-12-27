package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by buns on 12/27/15.
 */
public final class FreeMarkerUtils {
    /**
     * 创建文件名称
     *
     * @param templateRoot        [模板的根目录]
     * @param templateFilePath    [模板文件的地址]
     * @param destinationFileName [目标生成文件的地址]
     * @param templateParams      [模板解析时候需要的参数字典]
     * @return
     * @throws IOException
     */
    public static boolean createFile(String templateRoot, String templateFilePath, String destinationFileName, Map<String, Object> templateParams) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File(templateRoot));

        Writer writer = null;
        try {
            Template template = cfg.getTemplate(templateFilePath);

            File outputAbstract = new File(destinationFileName);

            writer = new FileWriter(outputAbstract);

            template.process(templateParams, writer);

            return true;

        } catch (TemplateException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
