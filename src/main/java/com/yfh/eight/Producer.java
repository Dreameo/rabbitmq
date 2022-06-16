package com.yfh.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 死信队列 生产者
 *
 */
public class Producer {

    // 普通交换机的名称
//    public static final String NORMAL_EXCHANGE = "normal_exchange"; //
    public static final String NORMAL_EXCHANGE = "mirror_hello"; //

    public static void main(String[] args) throws IOException, TimeoutException {
        // 发送死信消息 延迟消息 设置TTL时间 time to live
        Channel channel = RabbitUtils.getChannel();

        channel.queueDeclare(NORMAL_EXCHANGE, true, false, false, null);

        // 过期时间导致 死信
//        AMQP.BasicProperties properties = new AMQP.BasicProperties()
//                .builder().expiration("10000").build(); // 10s时间

        // 发10条消息
        for (int i = 1; i <= 10; i++) {
            String message = "info " + i;
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());
        }

    }
}
