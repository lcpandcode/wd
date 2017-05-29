package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/16.
 */

import cn.cpliang.wenda.util.JedisEventHandlerAdaptor;
import cn.cpliang.wenda.util.JedisKeyUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * 事件生产者
 */
@Component
public class EventProducer {
    @Autowired
    private JedisEventHandlerAdaptor jedisEventHandlerAdaptor;
    @Autowired
    private JedisKeyUtil jedisKeyUtil;

    public void add(EventModel model){
        String modelJson = JSONObject.toJSONString(model);
        jedisEventHandlerAdaptor.add(jedisKeyUtil.getEventHandlerKey(),modelJson);
    }
    public static void main(String [] args){
        EventModel model = new EventModel();
        model.setActionId(1);
        model.setActionOwnerId(2);
        model.setEntityId(3);
        model.setEventType(EventType.COMMENT);
        System.out.println(JSONObject.toJSONString(model));
    }
}
