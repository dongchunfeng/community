package com.nowcoder.community.service;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author MrDong
 */
@Service
public class CommentService implements CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectByEntity(entityType, entityId, offset, limit);
    }

    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectByCount(entityType, entityId);
    }

    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        //过滤敏感词
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int row = commentMapper.insertComment(comment);

        //更新贴子评论的数量
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int i = commentMapper.selectByCount(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), i);
        }
        return row;
    }

    public List<Comment> findReplyByUserId(int entityType, int userId, int offset, int limit) {
        return commentMapper.selectReplyByUserId(entityType, userId, offset, limit);
    }

    public int findReplyByCount(int entityType, int userId) {
        return commentMapper.selectReplyByCount(entityType, userId);
    }

    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }


}
