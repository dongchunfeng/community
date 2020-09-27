package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> selectByEntity(int entityType,int entityId,int offset,int limit);

    int selectByCount(int entityType,int entityId);

    int insertComment(Comment comment);

}
