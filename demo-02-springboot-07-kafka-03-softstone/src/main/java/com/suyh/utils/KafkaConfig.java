package com.suyh.utils;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka 配置类 这里不使用注解 @Configuration
 *
 * @author 李建华
 */
public class KafkaConfig {

    private static KafkaConfig instance = null;

    private KafkaConfig() {

    }

    /**
     * Kafka工具类 单列入口
     *
     * @return Kafka工具类
     */
    public static KafkaConfig getInstance() {
        if (instance == null) {
            instance = new KafkaConfig();
        }
        return instance;
    }

    private KafkaProducer producer = null;

    /**
     * 服务器列表  注意:这是kafka的地址，不是zookeeper
     * // @Value("${spring.kafka.bootstrap-servers}")
     */
    private String bootstrapServers;

    /**
     * 默认群组ID
     * // @Value("${spring.kafka.consumer.group-id}")
     */
    private String groupId;

    /**
     * 是否自动提交偏移量
     * // @Value("${spring.kafka.consumer.enable-auto-commit}")
     */
    private Boolean autoCommit;

    /**
     * #earliest--#当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
     * #latest ---当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
     * #none --topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
     * // @Value("${spring.kafka.consumer.auto-offset-reset}")
     */
    private String autoOffsetReset;

    /**
     * 一次调用poll()操作时返回的最大记录数，默认值为500
     * //@Value("${spring.kafka.consumer.max-poll-records}")
     */
    private Integer maxPollRecords;

    /**
     * 延时时间，延时时间到达之后计算批量发送的大小没达到也发送消息
     * //@Value("${spring.kafka.producer.linger}")
     */
    private int linger;

    /**
     * 如果该值大于零时，表示启用重试失败的发送次数
     * // @Value("${spring.kafka.producer.retries}")
     */
    private Integer retries;

    /**
     * #每当多个记录被发送到同一分区时，生产者将尝试将记录一起批量处理为更少的请求，
     * 49 #这有助于提升客户端和服务器上的性能，此配置控制默认批量大小（以字节为单位），默认值为16384
     * //@Value("${spring.kafka.producer.batch-size}")
     */
    private Integer batchSize;

    /**
     * 生产者可用于缓冲等待发送到服务器的记录的内存总字节数，默认值为33554432
     * //@Value("${spring.kafka.producer.buffer-memory}")
     */
    private Integer bufferMemory;

    public void initProducer(ConfigurableApplicationContext ctx) {

        ConfigurableEnvironment item = ctx.getEnvironment();
        bootstrapServers = item.getProperty("spring.kafka.bootstrap-servers");

        linger = item.getProperty("spring.kafka.producer.linger", Integer.class);
        retries = item.getProperty("spring.kafka.producer.retries", Integer.class);
        batchSize = item.getProperty("spring.kafka.producer.batch-size", Integer.class);
        bufferMemory = item.getProperty("spring.kafka.producer.buffer-memory", Integer.class);

        producer = new KafkaProducer<String, String>(producerConfigs());
    }

    public void initConsumer(ConfigurableApplicationContext ctx) {

        ConfigurableEnvironment item = ctx.getEnvironment();
        bootstrapServers = item.getProperty("spring.kafka.bootstrap-servers");

        groupId = item.getProperty("spring.kafka.consumer.group-id");
        autoCommit = item.getProperty("spring.kafka.consumer.enable-auto-commit", Boolean.class);
        autoOffsetReset = item.getProperty("spring.kafka.consumer.auto-offset-reset");
        maxPollRecords = item.getProperty("spring.kafka.consumer.max-poll-records", Integer.class);
        bufferMemory = item.getProperty("spring.kafka.producer.buffer-memory", Integer.class);
    }


    /**
     * 生产者配置生成
     *
     * @return map
     */
    public Map<String, Object> producerConfigs() {

        Map<String, Object> props = new HashMap<>();
        /**
         * 服务器地址或者集群地址列表
         */
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.getInstance().getBootstrapServers());
        /**
         * 设置重试次数
         */
        props.put(ProducerConfig.RETRIES_CONFIG, KafkaConfig.getInstance().getRetries());
        /**
         * 达到batchSize大小的时候会发送消息
         */
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, KafkaConfig.getInstance().getBatchSize());
        /**
         * 延时时间，延时时间到达之后计算批量发送的大小没达到也发送消息
         */
        props.put(ProducerConfig.LINGER_MS_CONFIG, KafkaConfig.getInstance().getLinger());
        /**
         * 缓冲区的值
         */
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, KafkaConfig.getInstance().getBufferMemory());
        /**
         * 序列化手段
         * properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         *  properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         */
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        /**
         * producer端的消息确认机制,-1和all都表示消息不仅要写入本地的leader中还要写入对应的副本中
         */
        props.put(ProducerConfig.ACKS_CONFIG, "-1");//单个brok 推荐使用'1'
        /**
         设置broker响应时间，如果broker在60秒之内还是没有返回给producer确认消息，则认为发送失败
         */
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 60000);
        /**
         *指定拦截器(value为对应的class)
         *props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "com.te.handler.KafkaProducerInterceptor");
         */

        /**
         * 设置压缩算法(默认是木有压缩算法的)
         */
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");//snappy
        return props;
    }

    /**
     * 消费者配置内容
     *
     * @return map
     */
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = Maps.newHashMap();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConfig.getInstance().getGroupId());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, KafkaConfig.getInstance().getAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConfig.getInstance().getAutoOffsetReset());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.getInstance().getBootstrapServers());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConfig.getInstance().getMaxPollRecords());
        //props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 180000);
        //props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 900000);
        //props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 900000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    //#region get 和set


    public KafkaProducer getProducer() {
        return producer;
    }

    public void setProducer(KafkaProducer producer) {
        this.producer = producer;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(Boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public Integer getMaxPollRecords() {
        return maxPollRecords;
    }

    public void setMaxPollRecords(Integer maxPollRecords) {
        this.maxPollRecords = maxPollRecords;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public Integer getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Integer bufferMemory) {
        this.bufferMemory = bufferMemory;
    }
    //#endregion
}
