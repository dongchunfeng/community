package com.nowcoder.community.util;

/**
 * 常量接口
 * @author MrDong
 */
public interface CommunityConstant {

    int ACTIVATION_SUCCESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAIL = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600;

    int REMEMBER_EXPIRED_SECONDS = 3600*24*365;

    //实体类型 帖子
    int ENTITY_TYPE_POST=1;

    //实体类型  评论
    int ENTITY_TYPE_COMMENT=2;

}
