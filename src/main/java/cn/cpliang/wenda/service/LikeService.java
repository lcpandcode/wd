package cn.cpliang.wenda.service;

import cn.cpliang.wenda.async.EventModel;
import cn.cpliang.wenda.async.EventProducer;
import cn.cpliang.wenda.async.EventType;
import cn.cpliang.wenda.model.EntityType;
import cn.cpliang.wenda.util.JedisEventHandlerAdaptor;
import cn.cpliang.wenda.util.JedisKeyUtil;
import cn.cpliang.wenda.util.LikeJedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;

/**
 * Created by lcplcp on 2017/5/12.
 */
@Service
public class LikeService {
    @Autowired
    LikeJedisAdapter likeJedisAdapter;
    @Autowired
    JedisKeyUtil jedisKeyUtil;
    @Autowired
    EventProducer eventProducer;
    //新增赞
    public long addLike(int userId,int entityType,int entityId) throws Exception {
        return likeJedisAdapter.add(jedisKeyUtil.getLikeKey(entityType,entityId),userId);
    }
    //新增踩
    public long addDislike(int userId,int entityType,int entityId) throws Exception {
        return likeJedisAdapter.add(jedisKeyUtil.getDislikeKey(entityType,entityId),userId);
    }
    //移除赞
    public long removeLike(int userId,int entityType,int entityId) throws Exception {
        return likeJedisAdapter.remove(jedisKeyUtil.getLikeKey(entityType,entityId),userId);
    }
    //移除踩
    public long removeDislike(int userId,int entityType,int entityId) throws Exception {
        return likeJedisAdapter.remove(jedisKeyUtil.getDislikeKey(entityType,entityId),userId);
    }
    //获取赞人数
    public long likeCount(int entityType,int entityId) throws Exception {
        return likeJedisAdapter.count(jedisKeyUtil.getLikeKey(entityType,entityId));
    }
    //获取踩人数
    public long dislikeCount(int entityType,int entityId) throws Exception {
        return likeJedisAdapter.count(jedisKeyUtil.getDislikeKey(entityType,entityId));
    }
    //根据用户id,entity判断该数据是否被赞
    public boolean isLikeByUserId(int userId,int entityType,int entityId) throws  Exception{
        return likeJedisAdapter.status(jedisKeyUtil.getLikeKey(entityType,entityId),userId);
    }
    //根据用户id,entity判断该数据是否被踩
    public boolean isDislikeByUserId(int userId,int entityType,int entityId) throws  Exception{
        return likeJedisAdapter.status(jedisKeyUtil.getDislikeKey(entityType,entityId),userId);
    }
}
