package cn.cpliang.wenda.util;

/**
 * Created by lcplcp on 2017/5/12.
 */

import org.springframework.stereotype.Component;

/**
 * jedis key生成工具类，针对不同的业务生成不同的key，作为redis存储相关数据的key
 */
@Component
public class JedisKeyUtil {
    //业务和实体类的分隔符
    private static final String SPLIT = ":";
    //表示点赞业务类型
    private static final String LIKE = "like";
    //表示踩实体业务类型
    private static final String DISLIKE = "dislike";
    //表示时间处理队列业务类型
    private static final String EVENT_HANDLER_QUEUE = "event_handler_queue";
    //表示注册激活用户的哈希结构数据



    private static final String REGISTER_ACTIVE_MAP = "register_active_map";
    //表示关注业务的存储set结构
    private static final String FOLLOWER = "follower";//粉丝列表
    private static final String FOLLOWEE = "followee";//关注列表

    public static String getFOLLOWER() {
        return FOLLOWER;
    }

    public static String getFOLLOWEE() {
        return FOLLOWEE;
    }

    public  String getLIKE() {
        return LIKE;
    }

    public  String getDISLIKE() {
        return DISLIKE;
    }

    public  String getEventHandlerQueue() {
        return EVENT_HANDLER_QUEUE;
    }

    public  String getRegisterActiveMap() {
        return REGISTER_ACTIVE_MAP;
    }

    public  String getSPLIT() {

        return SPLIT;
    }

    /**
     *
     * @param entityType 赞的类型，例如对问题和答案点赞，属于不同的entityType
     * @param entityId 赞的数据的id，例如对问题点赞则对应该问题的id
     * @return 返回业务key值
     */
    public String getLikeKey(int entityType,int entityId){
        return LIKE + SPLIT + entityType + entityId;
    }

    /**
     *
     * @param entityType 赞的类型，例如对问题和答案点赞，属于不同的entityType
     * @param entityId 赞的数据的id，例如对问题点赞则对应该问题的id
     * @return 返回业务key值
     */
    public String getDislikeKey(int entityType,int entityId){
        return DISLIKE + SPLIT + entityType + entityId;
    }

    public String getEventHandlerKey(){
        return EVENT_HANDLER_QUEUE;
    }
}
