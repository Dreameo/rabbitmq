package com.yfh.seven;

import com.rabbitmq.client.Channel;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Topic交换机
 * 生产者模式
 *
 */
public class EmitLogTopic {

    // 交换机名字
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 消息的通道 信道
        Channel channel = RabbitUtils.getChannel();

        Map<String, String> map = new HashMap<>();

    }
}
