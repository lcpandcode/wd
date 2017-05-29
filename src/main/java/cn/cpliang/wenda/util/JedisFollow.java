package cn.cpliang.wenda.util;

import cn.cpliang.wenda.model.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by lcplcp on 2017/5/19.
 */
@Component
public class JedisFollow {
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    JedisKeyUtil jedisKeyUtil;


    ///////////////////////////////////////////////////////////follower相关业务///////////////////////////////////////////////////////////////
    public long addFollower(int entityType, int entityId, int followerId) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zadd(jedisKeyUtil.getFOLLOWER() + entityType + entityId, new Date().getTime(), String.valueOf(followerId));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long removeFollower(int entityType, int entityId, int followerId) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zrem(jedisKeyUtil.getFOLLOWER() + entityType + entityId, String.valueOf(followerId));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public long countFollower(int entityType, int entityId) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zcard(jedisKeyUtil.getFOLLOWER() + entityType + entityId);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> getFollowerList(int entityType, int entityId, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return setToList(jedis.zrangeByLex(jedisKeyUtil.getFOLLOWER() + entityType + entityId, "-", "+", offset, count));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    private List<String> setToList(Set<String> set) {
        ArrayList<String> list = new ArrayList<>();
        for (String str : set) {
            list.add(str);
        }
        return list;
    }

    /*
    public List<String> getBothFollowerList(int entityType, int entityIdFirst,int entityIdSecond, int offset, int count) {
        Jedis jedis = null;
        try {
            jedisUtil.getJedisPool().getResource();
            return setToList(jedis.z(jedisKeyUtil.getFOLLOWER() + entityType + entityId, "-", "+", offset, count));
        } finally {
            jedis.close();
        }
    }
*/
    ///////////////////////////////////////////////////////////followee相关业务///////////////////////////////////////////////////////////////
    public long addFollowee(int userId, int entityType, int followeeId) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zadd(jedisKeyUtil.getFOLLOWEE()+ userId +entityType , new Date().getTime(), String.valueOf(followeeId));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long removeFollowee(int userId,int entityType,  int followeeId) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zrem(jedisKeyUtil.getFOLLOWEE() + userId+ entityType , String.valueOf(followeeId));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


    public long countFollowee(int userId, int entityType) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.zcard(jedisKeyUtil.getFOLLOWEE() + userId +entityType);
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> getFolloweeList(int userId, int entityType, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return setToList(jedis.zrangeByLex(jedisKeyUtil.getFOLLOWEE() + userId + entityType, "-", "+", offset, count));
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


}
