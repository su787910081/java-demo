package com.suyh;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class RedisTest {

    /**
     * 单点连接
     */
    @Test
    public void test01() {
        Jedis jedis = new Jedis("192.168.159.135", 6381);

        if (jedis.exists("name")) {
            String result = jedis.get("name");
            System.out.println("name: " + result);
        } else {
            jedis.set("name", "suyh");
            System.out.println("set name");
        }

        jedis.setex("color", 10, "red");

        jedis.close();
    }

    /**
     * Redis 集群操作
     */
    @Test
    public void test02() {
        // Redis 集群节点
        Set<HostAndPort> jedisClusterNode = new HashSet<>();
        jedisClusterNode.add(new HostAndPort("192.168.159.135", 7001));
        jedisClusterNode.add(new HostAndPort("192.168.159.135", 7002));
        jedisClusterNode.add(new HostAndPort("192.168.159.135", 7003));
        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxTotal(100);
        cfg.setMaxIdle(20);
        cfg.setMaxWaitMillis(-1);
        cfg.setTestOnBorrow(true);

        // timeout: 集群失效时间
        // maxAttempts: 最大重连次数
        JedisCluster jc = new JedisCluster(jedisClusterNode, 6000, 100, cfg);

        System.out.println(jc.set("age", "20"));
        System.out.println(jc.set("sex", "nv"));
        System.out.println(jc.get("name"));
        System.out.println(jc.get("age"));
        System.out.println(jc.get("sex"));

        jc.close();
    }
}
