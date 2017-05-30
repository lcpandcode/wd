package cn.cpliang.wenda.service;

/**
 * Created by lcplcp on 2017/5/19.
 */

import cn.cpliang.wenda.util.JedisFollow;
import cn.cpliang.wenda.util.LikeJedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关注服务类
 */
@Service
public class FollowService {
    @Autowired
    JedisFollow jedisFollow;

    /**
     * 关注方法
     *
     * @param entityType
     * @param entityId
     * @param followerId
     * @return
     */

    public boolean follow(int entityType, int entityId, int followerId) {
        boolean rtn = false;

        //同时更新粉丝列表和关注列表
        if (jedisFollow.addFollower(entityType, entityId, followerId) > 0 &&
                jedisFollow.addFollowee(followerId, entityType, entityId) > 0) {
            //发送邮件
            rtn = true;
        }
        return rtn;
    }

    public boolean unfollow(int entityType, int entityId, int followerId) {
        boolean rtn = false;

        //移除关注列表
        if (jedisFollow.removeFollower(entityType, entityId, followerId) > 0 &&
                jedisFollow.removeFollowee(followerId, entityType, entityId) > 0) {
            //发送邮件
            rtn = true;
        }
        return rtn;
    }

    public long countFollower(int entityType, int entityId) {
        return jedisFollow.countFollower(entityType, entityId);
    }


    public List<String> getFollowerList(int entityType, int entityId, int offset, int limit) {
        return jedisFollow.getFollowerList(entityType, entityId, offset, limit);
    }

    public long coutFollowee(int userId, int entityType) {
        return jedisFollow.countFollowee(userId, entityType);
    }

    public List<String> getFolloweeList(int userId, int entityType, int offset, int limit) {
        return jedisFollow.getFolloweeList(userId, entityType, offset, limit);
    }

}
