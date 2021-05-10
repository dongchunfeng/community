package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    /**
     * 查询所有帖子并分页
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<DiscussPost> selectAllDiscussPost(int userId, int offset, int limit);

    /**
     * 查询帖子总数   有且只有一个参数并且在<if></>中使用时需加上@Param 写上别名
     * @param userId
     * @return
     */
    int selectCount(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateDiscussCount(int id,int commentCount);

    int updateType(int id,int type);

    int updateStatus(int id,int status);

    int updateScore(int id,double score);

}
