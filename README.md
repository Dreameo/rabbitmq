

## 1、MQ的相关概念

### 1.1 什么是MQ



### 1.2 为什么要用MQ

> 三大作用： 1. 流量削峰  2. 异步处理 3. 解耦



#### 1.2.1 流量削峰



![image-20220614150952714](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614150952714.png)

好处：排队，都可以下单

缺点：排队，性能差



#### 1.2.2 应用解耦

![image-20220614151427821](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614151427821.png)

> 前： 调取其他子系统， 子系统出现故障， 订单系统一样会出现问题
>
> 后：订单 与队列 再去访问 ，订单系统完成， 发消息队列消息



#### 1.2.3 异步处理

![image-20220614151435945](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614151435945.png)



## 1.3 RabbitMQ



![image-20220614152403872](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614152403872.png)



### 1.3.3 RabbitMQ核心部分







### 代码过程

![image-20220614155022906](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614155022906.png)





### 工作队列 

注意事项： 一个消息只能处理一次， 不能处理多次

线程之间是竞争关系，进行轮询

![image-20220614162148613](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614162148613.png)





### 消息应答

防止消息丢失

![image-20220614164516555](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614164516555.png)

自动应答的环境：相对不是很靠谱，必须有一个良好的环境。



手动应答（一个肯定确认， 两个否定确认）

![image-20220614164833867](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614164833867.png)



### 消息自动重新入队：



当一个消费者消费的时间比较长，消费过程中出现宕机，消息会重新入队给其他消费者进行消费



![image-20220614165400204](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614165400204.png)





### RabbitMQ持久化

消息不丢失

![image-20220614173538686](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614173538686.png)



队列持久化和消息持久化



只是设置了队列持久化，没有设置消息持久化的话，不能保证消息不会被丢失





### 不公平分发

接受消息的时候设置参数

channel.basicQos(1) 能者多劳， 充分发挥每一台机器的性能，分发性能最高。



### 预取值

提前设置信道上的数据， 不管处理快还是慢 都是设置好的 消息条数 （消息必须是囤积在信道中， 而不是消费掉的， 排了队不能被其他消费者处理）

![image-20220614175209038](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614175209038.png)



### 发布确认

只有做了这三个步骤才能确保消息不丢失

消息不丢失的重要操作

1. 保证rabbitmq宕机了 队列不丢失
2. 生产者发送的消息可能会丢失
3. 传送过程中 保存到磁盘中上才能实现持久化目标， 还没来得及保存到磁盘上，就宕机，保存在磁盘上之后才与rabbitmq发送确认

![image-20220614201500925](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614201500925.png)



分三个情况：

```java
   channel.confirmSelect(); // 信道开启确认模式 (发一条确认测试？发一批确认测试？异步？)
/**
 * 三种发布确认模式
 *  使用的时间， 比较哪种确认方式最好
 *  1. 单个确认   1563 同步一个一个发消息，性能差一点
 *  2. 批量确认   169 一批一批 其中有一个出现问题， 并不知道到底是哪一个出现问题
 *  3. 异步确认   79   114
 *
 */

```

异步通知： 只管批量发送， mq消息体会通知我们哪个消息出现问题。对消息进行标记map(key,value)， 出现问题重新发送

![image-20220614211044909](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614211044909.png)





## 交换机



前面默认的交换机 一个消费者只能消费一个消息， 一个消息消费之后，不能在此消费，消费者之间是竞争关系。



现在需求，一个消息可以被多个消费者同时消费。

![image-20220614211540647](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614211540647.png)



生产者生产的消息不会直接发送到队列，生产者只能将消息发送给交换机，

![image-20220614211719673](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614211719673.png)

交换机根据 路由key 绑定队列



消息到达 交换机 ，通过key（用路由key）进行路由 将信息发送给特定的队列 队列



### Fanout(扇出), 广播模式

生产者 生产一个消息 所有消费者都能接受到消息， 因为交换机与队列绑定的key相同 

![image-20220614212644652](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614212644652.png)



### **直接交换机Direct**

RoutingKey不一样 ，绑定关系不一样， 按照规则给队列发消息

![image-20220614214150726](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220614214150726.png)





### topic交换机



更加灵活，direct只能路由一个队列， 不能同时路由，可以捆绑多个，但是没办法发送多个。



![image-20220616092559548](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616092559548.png)



![image-20220616092738193](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616092738193.png)





## 死信队列

无法被消费的消息

![image-20220616094902470](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616094902470.png)



![image-20220616094943203](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616094943203.png)



一直未付款： 死信队列实现



## 延迟队列

延迟队列是 死信队列的一种

> 消息TTL过期造成私信队列的情况  —— 》 延迟队列

使用场景：

![image-20220616111112527](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616111112527.png)



存在问题：

> 只检查第一个消息是否过期， 不管第二消息消息过期时间 长短

![image-20220616153221572](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616153221572.png)



### 延迟插件

> 现在消息 放在交换机 在交换机中延迟

![image-20220616153810575](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616153810575.png)



![image-20220616153848172](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616153848172.png)





## 高级发布确认



发消息是发给交换机的， 如果出现问题， 也是交换机出现问题， 如果出现问题，消息发不出去，将消息保存下来， 生产者发送消息给 交换机， 如果有问题 ，生产者能够感知交换机是否出现问题。 生产者发送消息 交换机不能接收消息， 调用回调函数 重新发送消息



回退消息：

交换机 路由不到 队列 设置mandatory参数， 交换机送不到， 那就将消息送回去 配置文件 设置

```properties
# 发布消息成功到交换机 后会触发回调方法
spring.rabbitmq.publisher-confirm-type=correlated 

# 发布退回
spring.rabbitmq.publisher-returns=true
```



## 备份交换机

> 备份、报警

不用回退给 生产者 重新发送

回退发给备用交换机 让备用交换机 使用备用交换机的备用队列





![image-20220616174254188](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616174254188.png)



**备用交换机的优先级 比 回退消息 高**



## 幂等问题

消息重复消费



## 优先级队列

![image-20220616184118992](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616184118992.png)

## 惰性队列： default 和 lazy模式

消费者宕机或下线

![image-20220616185111305](F:\开发\16_rabbitmq\rabbitmq.assets\image-20220616185111305.png)





## 镜像队列

> 集群中， 使用哪个节点声明 队列， 其他节点没有相同队列 ，在集群情况下， 其中一台节点宕机了，消息重启会消失。
>
> 因此考虑镜像队列



## 其他

idea 快捷键： 将字母快速变为 大小写 ctrl + shift + u

