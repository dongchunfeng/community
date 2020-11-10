package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/10/17
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> restTemplate = new RedisTemplate<>();
        restTemplate.setConnectionFactory(factory);
        //设置key value 的序列化方式
        restTemplate.setKeySerializer(RedisSerializer.string());
        restTemplate.setValueSerializer(RedisSerializer.json());
        restTemplate.setHashKeySerializer(RedisSerializer.string());
        restTemplate.setHashValueSerializer(RedisSerializer.json());
        //生效
        restTemplate.afterPropertiesSet();
        return restTemplate;
    }

}
