package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/10/23
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(likeKey, userId);
        if(member){
            redisTemplate.opsForSet().remove(likeKey,userId);
        }else{
            redisTemplate.opsForSet().add(likeKey,userId);
        }
    }

    /**
     * 查询某个实体的点赞数量
     * @param entityType
     * @param entityId
     * @return long
     */
    public long findEntityLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    /**
     * 查看某人对某个实体的点赞状态
     * @param entityType
     * @param entityId
     * @return int
     */
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(likeKey,userId)? 1 : 0;
    }

}
