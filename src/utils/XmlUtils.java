package utils;

import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.dyuproject.protostuff.*;

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
     * @param message
     * @param filePath
     * @param <T>
     */
    public static <T extends Object&Serializable> void serialize(T message,String filePath) throws IOException {
        if (filePath.isEmpty() || message == null) {
            return;
        }
        Schema<T> msgSchema = (Schema<T>) RuntimeSchema.getSchema(message.getClass());

        byte[] msgBytes = XmlIOUtil.toByteArray(message, msgSchema);

        OutputStream out = new FileOutputStream(filePath);

        out.write(msgBytes);

        out.close();
    }

    /**
     * 将xml配置文件中的信息反序列化到对象中
     * @param filePath
     * @param <T>
     * @return
     */
    public static <T extends Object&Serializable> T deserialize(Class<T> tType,String filePath) throws IOException, IllegalAccessException, InstantiationException {

        if (filePath.isEmpty()) {
            return null;
        }

        InputStream input = new FileInputStream(filePath);

        int size = input.available();

        byte[] bytes = new byte[size];

        input.read(bytes);

        T instance = tType.newInstance();

        Schema<T> msgSchema = RuntimeSchema.getSchema(tType);

        XmlIOUtil.mergeFrom(bytes, instance, msgSchema);

        return instance;
    }
}
