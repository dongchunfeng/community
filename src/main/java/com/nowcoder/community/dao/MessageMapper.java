package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author MrDong
 */
@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户的私信列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectConversation(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话次数
     * @param userId
     * @return
     */
    int selectConversationCount(int userId);

    /**
     * 查询某个用户的会话详情
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectLetters(String conversationId,int offset,int limit);

    /**
     * 查询某个会话的私信数量
     * @param conversationId
     * @return
     */
    int selectLettersCount(String conversationId);

    /**
     * 查询未读的私信数量
     * @param userId
     * @param conversationId
     * @return
     */
    int selectLetterUnreadCount(int userId,String conversationId);

    /**
     * 添加一条私信
     * @param message
     * @return
     */
    int insertLetter(Message message);

    /**
     * 根据多个id修改状态
     * @param ids
     * @param status
     * @return
     */
    int updateLetterByIds(List<Integer> ids,int status);

    /**
     * 查询某个主题下的最新通知
     * @param userId
     * @param topic
     * @return
     */
    Message selectLatestNotice(int userId,String topic);

    /**
     * 查询某个主题下的通知数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeCount(int userId,String topic);

    /**
     * 查询某个主题下未读的通知数量
     * @param userId
     * @param topic
     * @return
     */
    int selectNoticeUnreadCount(int userId,String topic);


    /**
     * 查询某个主题下详情的通知
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectNotices(int userId,String topic,int offset,int limit);


}
