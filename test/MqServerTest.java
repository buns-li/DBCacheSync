import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2015/12/23.
 */
public class MqServerTest {

    private static final String EXCHANGE_NAME = "relax:redis";

    public static void main(String[] args) throws IOException {

        ConnectionFactory fac = new ConnectionFactory();

        fac.setHost("139.196.169.139");

        fac.setUsername("yxadmin");

        fac.setPassword("yxadmin");

        fac.setPort(AMQP.PROTOCOL.PORT);

        Connection conn = null;

        try {
            conn = fac.newConnection();

            Channel channel = conn.createChannel();

            //String[] routing_keys = new String[] {"cache.opt","code.opt","testResult"};

            //压缩包类型dev,prod

            String routing_keys = "code.createLine";
            String idAll = "1000:1001";

            channel.basicPublish(EXCHANGE_NAME, routing_keys, null, idAll
                    .getBytes());

            channel.close();

            conn.close();

            System.out.println("消息发送成功");

        } catch (IOException e) {

            e.printStackTrace();

        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
