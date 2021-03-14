package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void like(int userId, int entityType, int entityId,int entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                //某个实体的赞
                String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                //某个用户的赞
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                Boolean member = redisOperations.opsForSet().isMember(likeKey, userId);
                //开启事务
                redisOperations.multi();
                if (member) {
                    redisOperations.opsForSet().remove(likeKey, userId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                } else {
                    redisOperations.opsForSet().add(likeKey, userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }
                return redisOperations.exec();
            }
        });
    }

    /**
     * 查询某个实体的点赞数量
     *
     * @param entityType
     * @param entityId
     * @return long
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(likeKey);
    }

    /**
     * 查看某人对某个实体的点赞状态
     *
     * @param entityType
     * @param entityId
     * @return int
     */
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(likeKey, userId) ? 1 : 0;
    }


    /**
     * 查询用户获取的赞
     *
     * @param userId
     * @return
     */
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count.intValue();
    }
}
