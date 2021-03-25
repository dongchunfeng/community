package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2021/3/24 12:44
 */
@Service
public class DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

    /**
     * 将制定的ip计入uv
     *
     * @param ip
     */
    public void recordUv(String ip) {
        String uvKey = RedisKeyUtil.getUvKey(df.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(uvKey, ip);
    }

    /**
     * 统计指定范围内的uv
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public long calculateUv(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        List<String> list=  new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        //起始时间小于结束时间
        while(!calendar.getTime().after(endDate)){
            String key = RedisKeyUtil.getUvKey(df.format(calendar.getTime()));
            list.add(key);
            calendar.add(Calendar.DATE,1);
        }

        //合并数据
        String redisKey = RedisKeyUtil.getUvKey(df.format(startDate),df.format(endDate));
        redisTemplate.opsForHyperLogLog().union(redisKey,list.toArray());


        //返回统计的结果
        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }


    public void recordDau(Integer userId) {
        String dauKey = RedisKeyUtil.getDauKey(df.format(new Date()));
        redisTemplate.opsForValue().setBit(dauKey, userId,true);
    }

    public long calculateDau(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("参数为空!");
        }
        List<byte[]> list=  new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        //起始时间小于结束时间
        while(!calendar.getTime().after(endDate)){
            String key = RedisKeyUtil.getDauKey(df.format(calendar.getTime()));
            list.add(key.getBytes());
            calendar.add(Calendar.DATE,1);
        }

        //合并数据
        //返回统计的结果
        return (long)redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                String key = RedisKeyUtil.getDauKey(df.format(startDate),df.format(endDate));
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR,key.getBytes(),list.toArray(new byte[0][0]));
                return redisConnection.bitCount(key.getBytes());
            }
        });
    }

}
