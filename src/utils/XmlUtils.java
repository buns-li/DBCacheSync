package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;

/**
 * Created by Administrator on 2015/12/23.
 *
 * XML文件序列化与反序列化
 *  采用protoStuff-xml的形式
 *
 */
public class XmlUtils{

    /**
     * 序列化对象至Xml文件
     * @param message java bean对象
     * @param filePath xml文件路径
     * @param <T> 要进行序列化的java bean对象类型
     */
    public static <T> void serialize(T message, String filePath) {
        if (filePath.isEmpty() || message == null) {
            return;
        }

        XStream xStream = new XStream(new DomDriver());

        xStream.processAnnotations(message.getClass()); //通过注解方式的，一定要有这句话

        try {

            OutputStream out = new FileOutputStream(filePath);

            xStream.toXML(message, out);

            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将xml配置文件中的信息反序列化到对象中
     * @param filePath xml文件路径地址
     * @param <T> 要发序列化形成的对象类型
     * @return java对象
     */
    public static <T> T deserialize(Class<T> tType, String filePath) {

        if (filePath.isEmpty()) {
            return null;
        }

        try {
            InputStream input = new FileInputStream(filePath);

            XStream xStream = new XStream(new DomDriver());

            xStream.processAnnotations(tType);

            return (T) xStream.fromXML(input);

        } catch (IOException e) {
            System.out.println(e);
            return null;
        }

    }
}
