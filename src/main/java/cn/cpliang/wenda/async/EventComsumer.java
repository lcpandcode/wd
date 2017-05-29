package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/15.
 */

import cn.cpliang.wenda.controllor.MessageController;
import cn.cpliang.wenda.util.JedisEventHandlerAdaptor;
import cn.cpliang.wenda.util.JedisKeyUtil;
import cn.cpliang.wenda.util.ThreadPoolUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 事件处理类，该类负责调用handler，对事件进行处理，需要实现spring的两个接口，InitializingBean接口是初始化时自动注册handler要用；
 *ApplicationContextAware则是调用spring的applicationContext（该applicationContext中存储着handler具体实现类的bean对象）需要实现
 * 的接口，通过applicationContext获取handler对应的beans,然后就可以将handler自动注册到下面的config对象中了（是一个map)
 */
@Component
public class EventComsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    /**
     * threadPoolUtil封装了线程池的线程相关操作
     */
    @Autowired
    private ThreadPoolUtil threadPool;
    @Autowired
    private JedisEventHandlerAdaptor adaptor;
    /**
     * 这是一个与redis交互相关的工具类，用于获取特定的redis key，避免key冲突用，读者可以忽略
     */
    @Autowired
    private JedisKeyUtil jedisKeyUtil;
    /**
     * spring上下文对象，该对象存储着handler bean对象，必须通过setApplicationContext（ApplicationContextAware接口的实现方法）
     * 进行初始化，这样才能获取spring中的handler具体实现类的beans
     */
    private ApplicationContext applicationContext;
    /**设置消费函数阻塞时间，暂定为一天，redis阻塞list中必须要的参数，读者可以忽略
     */
    private static int COMSUME_TIMEOUT = 24*3600;
    /**
     * congif:该变量用于存储type和eventType的映射关系，在消费时，可以直接根据config中你的映射关系进行handler调用
     * 注意，这里为了保证程序的灵活性，eventHandler用一个list进行存储，因为有可能一个EventType事件类型可能对应多个
     * handler事件处理对象，例如点赞通知这个事件类型可能需要通知被点赞的人以及通知系统管理员，所以应该对应两个事件handler
     * 更具体的可以参考handler接口设计时的注释
     */
    Map<EventType,List<EventHandler>> config = new HashMap<>();

    /**
     * spring对该对象进行初始化的时候，将所有的handler具体对象注册到config对象中
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
            EventHandler handler = entry.getValue();
            for(EventType type:handler.getHandlerType()){
                if(config.get(type)==null){
                    config.put(type,new ArrayList<EventHandler>());
                }
                config.get(type).add(handler);
            }
        }
        //开线程调用消费函数，注意不能直接调用，否则会导致主线程阻塞
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                doConsume();
            }
        });


    }

    /**
     * 消费函数，用于执行handler
     */
    public void doConsume() {
        while(true){
            List<String> list = adaptor.pop(String.valueOf(COMSUME_TIMEOUT), jedisKeyUtil.getEventHandlerKey());
            //反序列化
            EventModel model = JSON.parseObject(list.get(1),EventModel.class);
            EventType type = model.getEventType();
            //获取事件的handler
            List<EventHandler> handlers = config.get(type);
            //执行handler
            for(EventHandler handler:handlers){
                handler.doHandler(model);
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void main(String [] args){
        EventComsumer comsumer = new EventComsumer();
        //反序列化测试
        EventModel model = new EventModel();
        model.setActionId(1);
        model.setActionOwnerId(2);
        model.setEntityId(3);
        model.setEventType(EventType.COMMENT);
        String text = JSONObject.toJSONString(model);
        JSONObject model_json = JSONObject.parseObject(text);
        EventModel model1 = JSON.parseObject(text, EventModel.class);
        System.out.println(model1.getActionId());
    }
}
