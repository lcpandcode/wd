package cn.cpliang.wenda.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/17.
 */
@Component
public class JedisActive {
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    JedisKeyUtil jedisKeyUtil;

    /**
     * 激活用户
     * @param activeId
     * @param userId
     * @return
     */
    public boolean active(String activeId,String userId){
        boolean rtn = false;
       //根据userId取出activeId
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            String activeSystem = jedis.hget(jedisKeyUtil.getRegisterActiveMap(),userId);
            if(activeSystem!=null && activeSystem.equals(activeId)){
                rtn = true;
            }
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
        return rtn;
    }

    public long put(String activeId,String userId){
        //根据userId取出activeId
        Jedis jedis = null;
        try {
            jedis = jedisUtil.getJedisPool().getResource();
            return jedis.hset(jedisKeyUtil.getRegisterActiveMap(),userId,activeId);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


}
