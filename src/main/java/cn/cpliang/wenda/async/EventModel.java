package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/14.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * 事件模型：用于表示个事件
 */
public class EventModel {
    /**
     * 事件类型，用于标识事件，同时在comsumer中根据这个值确定handler的具体实现类，一般可用一个枚举类型实现
     * 例如点赞通知对应的事件类型和注册发邮件进行激活的事件就应该属于不同的eventType，应该对应不同的handler实现类
     */
    private EventType eventType;
    /**
     * 事件触发者，例如用户A给用户B点赞，A就是时间触发者
     */
    private int actionId;
    /**
     * entityType用于标识不同的业务
     */
    private int entityType;
    private int entityId;
    /**
     * 事件发生对应的关联者，例如A给B点赞，A对应actionId,actionOwnerId
     */
    private int actionOwnerId;
    /**时间处理需要的额外的数据，采用map的方式可以保证程序的扩展性
     * 例如注册发送邮件的操作需要的数据和点赞通知需要的数据并不一样，所以用map存储最大程度地保证程序的灵活性
     */
    private Map<String,String> exts = new HashMap<>();

    /**
     * 注意序列化需要显式有一个无参构造函数
     */
    public EventModel(){

    }

    /**
     * getter 和setter，这部分省略
     */
    public EventType getEventType() {
        return eventType;
    }

    public EventModel setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getActionId() {
        return actionId;
    }

    public EventModel setActionId(int actionId) {
        this.actionId = actionId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;return this;
    }

    public int getActionOwnerId() {
        return actionOwnerId;
    }

    public EventModel setActionOwnerId(int actionOwnerId) {
        this.actionOwnerId = actionOwnerId;return this;
    }

    public Object getExts(String key) {
        return exts.get(key);
    }

    public EventModel setExts(String key,String value) {
        exts.put(key,value);return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;
    }
}
