package com.suyh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

@RestController
public class JedisController {
    @Autowired
    private JedisCluster cluster;

    @RequestMapping("/jedis")
    public String clusterSetGet(String key, String value) {
        cluster.set(key, value);
        return cluster.get(key);
    }
}
