package com.dist.redfx.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;


/**
 * Simple service for working with Redis
 */
public class RedisService {
    private String hostname;
    private Jedis jedis;
    private Gson gson;

    public RedisService(String hostname) {
        this.hostname = hostname;
        this.jedis = new Jedis(hostname);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public RedisService(String hostname, int port) {
        this.hostname = hostname;
        this.jedis = new Jedis(hostname, port);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String ping() {
        return jedis.ping();
    }

    public void close() {
        jedis.close();
    }

}