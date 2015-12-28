package servers.mqServer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import core.PlatGlobal;
import core.Processor;
import core.ServerBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2015/12/23.
 *
 * 消息队列监听服务
 *  1.先启动服务端的消息主题监听
 *  2.提供对外的
 */
public class MessageQueueServer extends ServerBase {

    private static Map<String, Processor> EVENTS = new HashMap<String, Processor>();

    /**
     * 注册一个消息队列的路由处理
     *
     * @param key
     * @param pro
     */
    public static final void registerRoute(String key, Processor pro) {
        EVENTS.put(key, pro);
    }

    @Override
    public void run() throws Exception{
        System.out.println("servers.mqServer begin running...");

        Properties configs = PlatGlobal.instance().getPlatformConfig();

        String exchangeName = configs.get("msg_defaultExchangeName").toString();

        //向rabbitMQ消息队列的处理流程中嵌入代码文件处理的逻辑操作
        // 创建连接和频道
        ConnectionFactory fac = new ConnectionFactory();

        fac.setHost(configs.get("msg_host").toString());
        fac.setPort(Integer.parseInt(configs.get("msg_port").toString()));
        fac.setUsername(configs.get("msg_username").toString());
        fac.setPassword(configs.get("msg_password").toString());

        Connection connection = fac.newConnection();

        Channel channel = connection.createChannel();

        String queueName = exchangeName+":queue";

        //接收所有与kernel相关的消息
        channel.queueDeclare(queueName,true,false,false,null);

        channel.queueBind(queueName, exchangeName, "cache.*");
        channel.queueBind(queueName, exchangeName, "code.*");
        channel.queueBind(queueName, exchangeName, "test.*");

        System.out.println("servers.mqServer running Success and Waiting for client messages!\n");

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName, true, consumer);

        while (true) {

            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            //cache格式： tbno:id
            //code格式:  appid、appid:lineids、appid:lineid:nodeids
            String message = new String(delivery.getBody());

            String routingKey = delivery.getEnvelope().getRoutingKey();

            System.out.println("Received routingKey = " + routingKey
                    + ",msg = " + message + ".");
            Processor proc = EVENTS.get(routingKey);
            if(proc!=null){
                proc.doProcess(message);
            }
        }

    }
}
