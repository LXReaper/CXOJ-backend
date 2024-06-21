package com.yp.CXOJ.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 余炎培
 * @since 2024-06-16 星期日 23:06:22
 * redis配置类
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Autowired
    public static RedisTemplate<String, String> redisTemplate;

    /**
     * key为String，value为String的配置
     * @return
     */
    @Bean
    public RedisTemplate<String,String> setSTSRedisConfig(){
        redisTemplate = new RedisTemplate<>();
        //解决序列化问题(很坑)
        redisTemplate.setDefaultSerializer(StringRedisSerializer.UTF_8);//默认的序列化程序
        redisTemplate.setKeySerializer(new StringRedisSerializer());//序列化键
        redisTemplate.setValueSerializer(new StringRedisSerializer());//序列化值

        //Java连接redis的工厂类
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setUsePool(true);//使用连接池
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.afterPropertiesSet();//必须填,因为前面的设置后，需要调用afterPropertiesSet方法来完成初始化

        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();//必须填,因为前面的设置后，需要调用afterPropertiesSet方法来完成初始化
        return redisTemplate;
    }
}
