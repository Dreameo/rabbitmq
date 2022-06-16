package com.yfh.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.yfh.utils.RabbitUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 三种发布确认模式
 *  使用的时间， 比较哪种确认方式最好
 *  1. 单个确认   1563 同步一个一个发消息，性能差一点
 *  2. 批量确认   169 一批一批 其中有一个出现问题， 并不知道到底是哪一个出现问题
 *  3. 异步确认   79   114
 *
 */
public class ConfirmMessage {

    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 1. 单个确认
//        publicMessageIndividually();

        // 2. 批量确认
//        publicMessageBatch();

        // 3. 异步确认
        publicMessageAsync();


    }


    public static void publicMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtils.getChannel();

        // 队列声明
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        // 开启确认发布
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 单个消息发送完 就马上进行发布确认
            boolean flag = channel.waitForConfirms();

            if (false) {
                System.out.println("消息发送成功！");
            }
        }

        long end = System.currentTimeMillis();

        // 总共耗时
        System.out.println("发布" + MESSAGE_COUNT + "个消息 单个确认模式下 耗时：" + (end - begin));

    }

    public static void publicMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtils.getChannel();

        // 队列声明
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        // 开启确认发布
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量确认消息的大小
        int batchSize = 100;



        // 批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 判断消息 100 条时 批量确认一次
            if((i + 1) % batchSize == 0) {
                // 发布确认
                channel.waitForConfirms();
            }

        }

        long end = System.currentTimeMillis();

        // 总共耗时
        System.out.println("发布" + MESSAGE_COUNT + "个消息 批量确认模式下 耗时：" + (end - begin));

    }


    public static void publicMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitUtils.getChannel();

        // 队列声明
        String queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName, true, false, false, null);

        // 开启确认发布
        channel.confirmSelect();


        // 批量确认消息的大小
        int batchSize = 100;


        /**
         *
         * 线程安全有序的一个哈希表， 适用于高并发的情况下
         * 1. 轻松地将序号与消息进行关联
         * 2. 轻松批量删除条目， 只要给出需要
         * 3. 支持高并发（多线程）
         *
         */

        ConcurrentSkipListMap<Long, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();



        // 发消息之前 准备一个消息的监听器， 监听哪些消息成功， 哪些消息失败了

        // 监听成功的消息
        // 1. 消息的标记
        // 2. 是否为批量确认
        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
            System.out.println("确认成功" + deliveryTag);
            // 删除确认消息
            if(multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = concurrentSkipListMap.headMap(deliveryTag); // 确认消息的
                confirmed.clear();
            } else {
                concurrentSkipListMap.remove(deliveryTag);
            }



        };
        // 监听失败的消息
        ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
            System.out.println("未确认成功的消息" + deliveryTag);
        };

        channel.addConfirmListener(confirmCallback, nackCallback);



        // 批量发消息
        // 开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            // 1. 记录所有要发送的消息， 消息的综合
            concurrentSkipListMap.put(channel.getNextPublishSeqNo(), message); // 信息信号
            // 2. 在确认监听中， 删除已经确认的消息， 剩下的就是未确认的消息
            // 3.

            // 不进行确认

        }

        long end = System.currentTimeMillis();

        // 总共耗时
        System.out.println("发布" + MESSAGE_COUNT + "个消息 异步确认模式下 耗时：" + (end - begin));

    }
}
