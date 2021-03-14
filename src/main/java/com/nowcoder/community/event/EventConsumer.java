package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 消费者  发送通知
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/10 17:16
 */
@Component
@Slf4j
public class EventConsumer implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_LIKE})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息的格式错误");
            return;
        }

        //发送消息  把  event  封装进Message的content中
        Message message = new Message();
        message.setFromId(SYSTEM_USER);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<String, Object>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));

        messageService.addMessage(message);

    }


}
