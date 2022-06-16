package com.yfh.five;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLog02 {


    // 交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = RabbitUtils.getChannel();

        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout"); // 扇出类型

        // 声明一个队列  临时队列

        /**
         *
         * 生成一个临时队列， 队列的名称随机
         * 当消费者断开与队列的连接之后，队列自动删除
         */
        String queue = channel.queueDeclare().getQueue();

        /**
         * 绑定交换机与队列
         */

        channel.queueBind(queue, EXCHANGE_NAME, "");

        System.out.println("Recevier02等待接收消息， 把接受的消息打印到屏幕上....");


        // 接收消息 成功消息 回调接口
        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.println("Recevier02接受到的消息:" + new String(message.getBody()));
        });

        // 消费者 取消消息时回调接口
        CancelCallback cancelCallback = (consumerTag -> {
            System.out.println("取消消息 逻辑！");
        });


        channel.basicConsume(queue, true, deliverCallback, cancelCallback);
    }
}
