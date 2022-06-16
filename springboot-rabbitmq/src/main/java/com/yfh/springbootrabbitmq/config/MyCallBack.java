package com.yfh.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    // 实现的是内部接口

    // 将 注入

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct // 其他 方法执行完之后， 才能执行
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     *
     * 交换机确认 回调函数
     * 1. 发送消息  交换机接收到了  回调
     *      1.1 correlationData 保存回调消息的ID和相关消息
     *      1.2 交换机收到消息 ack = true
     *      1.3 cause null
     * 2. 发送消息 交换机接受失败了  回调
     *      2.1 correlationData 保存回调消息的ID和相关消息
     *      2.2 交换机收到消息 ack= false
     *      2.3 cause 失败的原因
     * @param correlationData
     * @param ack
     * @param cause
     */

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到ID为: {} 的消息", id );
        } else {
            log.info("交换机未收到ID为: {} 的消息， 由于原因{}", id, cause );
        }
    }


    /**
     * 可以在当消息传递过程中 不可达目的地 时将 消息返回给生产者
     * 只有不可达目的地的时候 才能回退
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息{},被回退的原因为：{}, 路由key：{}",new String(message.getBody()), replyText, routingKey );
    }
}
