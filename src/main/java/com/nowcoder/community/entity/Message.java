package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Message {

    private int id;
    private int fromId;  //发送者
    private int toId;  //接受者
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

}
