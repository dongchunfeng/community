package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/10 17:02
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void fireEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }


}
