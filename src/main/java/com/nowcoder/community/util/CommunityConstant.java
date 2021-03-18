package com.nowcoder.community.util;

/**
 * 常量接口
 *
 * @author MrDong
 */
public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 激活重复
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAIL = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 365;

    /**
     * 实体类型 帖子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型  评论
     */
    int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型 用户
     */
    int ENTITY_TYPE_USER = 3;

    /**
     * 主题   评论
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 主题： 点赞
     */
    String TOPIC_LIKE = "like";

    /**
     * 主题： 关注
     */
    String TOPIC_FOLLOW = "follow";

    /**
     * 系统用户  默认为1
     */
    int SYSTEM_USER = 1;

    /**
     * 权限  普通用户
     */
    String AUTHORITY_USER = "user";

    /**
     * 管理员
     */
    String AUTHORITY_ADMIN = "admin";

    /**
     * 版主
     */
    String AUTHORITY_MODERATOR = "moderator";

}
