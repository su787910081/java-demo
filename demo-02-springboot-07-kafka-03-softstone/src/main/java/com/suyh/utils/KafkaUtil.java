package com.suyh.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Kafka工具类
 *
 * @author 李建华
 */
@Component
public class KafkaUtil {
    private static final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

    @Resource
    private ConfigurableApplicationContext ctx;

    /**
     * 创建一个kafka管理类，相当于rabbitMQ的管理类rabbitAdmin,
     * 自定义的使用adminClient创建topic
     *
     * @return KafkaAdmin kafka管理类
     */
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        /**
         * 配置Kafka实例的连接地址
         * kafka的地址，不是zookeeper
         */
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.getInstance().getBootstrapServers());
        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    /**
     * kafka客户端，创建topic,用于集群环境，创建对个副本
     *
     * @return AdminClient kafka客户端
     */
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }

    /**
     * 生产者工厂
     *
     * @return {@code ProducerFactory<String, String>} 生产者工厂
     */
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(KafkaConfig.getInstance().producerConfigs());
    }

    /**
     * 生产者工厂  先不使用些方法，这里只处理字符串方式，用json序列
     *
     * @return {@code ProducerFactory<String, String>} 生产者工厂
     */
    public ProducerFactory<String, Object> producerFactory1() {
        return new DefaultKafkaProducerFactory<>(KafkaConfig.getInstance().producerConfigs());
    }

    /**
     * 非注解bean直接调用原生kafka生产者方法
     *
     * @return {@code KafkaProducer<String, String>} 生产者工厂
     */
    public KafkaProducer<String, String> kafkaProducer() {
        // producer = new KafkaProducer<String, String>(KafkaConfig.getInstance().producerConfigs());
        return KafkaConfig.getInstance().getProducer();
    }

    /**
     * 生产者发送信息（生产信息)
     *
     * @param topic 标题
     * @param msg   要生成的信息内容字符串
     */
    @Async("asyncServiceExecutor")
    public void kafkaProducerSend(String topic, String msg) {
        kafkaProducerSend(topic, null, msg);
    }

    /**
     * 生产者发送信息（生产信息)
     *
     * @param topic 标题
     * @param key   信息key
     * @param msg   要生成的信息内容字符串
     */
    @Async("asyncServiceExecutor")
    public void kafkaProducerSend(String topic, String key, String msg) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, msg);
            KafkaConfig.getInstance().getProducer().send(record);
        } catch (Exception ex) {
            logger.error("发送kafka消息异常", ex);
        }
    }

    /**
     * 生产者发送信息（生产信息)
     * 内部进行json转换为字符串再发送,消费者拿到后要再转换成对应的实体
     * 此方法无返回，如果要回调可以再进行封装，或者再自行外部写方法
     *
     * @param topic 标题
     * @param msg   要生成实体对象，内部进行json转换为字符串再发送，
     */
    public void producerSend(String topic, Object msg) {
        String msgStr = JSONObject.toJSONString(msg);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, msgStr);
        kafkaTemplate().send(record);
    }

    /**
     * @return {@code  KafkaTemplate<String, String>}
     */
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    public KafkaListenerContainerFactory<?> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(KafkaConfig.getInstance().consumerConfigs()));
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        // set the retry template
//        factory.setRetryTemplate(retryTemplate());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * /**
     * 手动创建topic和分区
     * 这种是手动创建 //5个分区，一个副本 分区多的好处是能快速的处理并发量，但是也要根据机器的配置
     * 如果topic的名字相同，那么会覆盖以前的那个；而分区数只能增加，不能减少的进行修改
     *
     * @param topicName         标题名称
     * @param numPartitions     分区数
     * @param replicationFactor 副本个数据
     */
    @Async
    public void createTopic(String topicName, int numPartitions, int replicationFactor) {
        try {
            NewTopic topic = new NewTopic(topicName, numPartitions, (short) replicationFactor);
            adminClient().createTopics(Arrays.asList(topic));
            //等待创建
            Thread.sleep(1000);
        } catch (Exception ex) {
            logger.error("创建Kafka标题topic异常", ex);
        }
    }

    /**
     * 异步开启监听消费者信息
     *
     * @param topics String[] topics = new String[]{"dev3-yangyunhe-topic001", "dev3-yangyunhe-topic002"};
     */
    @Async("asyncServiceExecutor")
    public void kafkaListenerConsumer(String[] topics) {
        //1.创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(KafkaConfig.getInstance().consumerConfigs());
        //String[] topics = new String[]{"dev3-yangyunhe-topic001", "dev3-yangyunhe-topic002"};
        // 订阅指定主题的全部分区
        consumer.subscribe(Arrays.asList(topics));
        //指定分区
        //        List<TopicPartition> topicPartitions = new ArrayList<>();
        //        for(String topic : topics){
        //            List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        //            for (PartitionInfo info : partitionInfos) {
        //                TopicPartition topicPartition = new TopicPartition(info.topic(), info.partition());
        //                topicPartitions.add(topicPartition);
        //            }
        //        }
        //        consumer.assign(topicPartitions);
        try {
            while (true) {
                /*
                 * poll() 方法返回一个记录列表。
                 * 每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对。
                 * 我们一般会遍历这个列表，逐条处理这些记录。
                 * 传给poll() 方法的参数是一个超时时间，用于控制 poll() 方法的阻塞时间（在消费者的缓冲区里没有可用数据时会发生阻塞）。
                 * 如果该参数被设为 0，poll() 会立即返回，否则它会在指定的毫秒数内一直等待 broker 返回数据。
                 * 而在经过了指定的时间后，即使还是没有获取到数据，poll()也会返回结果。
                 */
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    //触发事件
//                    SpringContextUtil.getApplicationContext().publishEvent(new CustomEvent(this, record.topic(), record.value()));
                    ctx.publishEvent(new CustomEvent(this, record.topic(), record.value()));
                }
            }
        } catch (Exception ex) {
            logger.error("推送kafka异常 消费者", ex);
        } finally {
            /*
             * 在退出应用程序之前使用 close() 方法关闭消费者。
             * 网络连接和 socket 也会随之关闭，并立即触发一次再均衡，而不是等待群组协调器发现它不再发送心跳并认定它已死亡，
             * 因为那样需要更长的时间，导致整个群组在一段时间内无法读取消息。
             */
            consumer.close();
        }
    }
}
