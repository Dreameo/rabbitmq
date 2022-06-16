package com.yfh.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列： 消费者1
 *
 */
public class Consumer01 {
    // 两个交换机 一个普通交换机 一个死信交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange"; //
    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitUtils.getChannel();

        // 声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 声明队列

        // 声明普通队列的参数
        Map<String, Object> arguments = new HashMap<>();
        // 过期时间 导致的死信队列 10s

//        arguments.put("x-message-ttl", 10000); // 可以由生产者方 设置
        // 正常队列设置过期之后 死信交换机是谁
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);

        // 设置死信路由key
        arguments.put("x-dead-letter-routing-key", "lisi");

        // 设置正常队列的长度的限制

//        arguments.put("x-max-length", 6); // 更改了数据 需要去可视化界面 删除


        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        // 出现死信 ： 普通队列需要设置一些参数 才能将 消息发送到死信交换机

        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);


        // 绑定交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");



        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if("info 5".equals(msg)) {
                System.out.println("Consumer01拒绝了的消息：" + msg);
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01消费者接受到的消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }


        };

        CancelCallback cancelCallback = consumerTag -> {

        };

        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, cancelCallback);
    }


}
