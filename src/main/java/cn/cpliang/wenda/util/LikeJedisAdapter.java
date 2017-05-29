package cn.cpliang.wenda.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by lcplcp on 2017/5/11.
 */
@Component
public class LikeJedisAdapter implements InitializingBean {
    @Autowired
    JedisUtil jedisUtil;
    JedisPool jedisPool = null;

    //main 函数测试
    public static void main(String [] args){
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = jedisUtil.getJedisPool();
    }

    //新增
    public long add(String key,Integer userId) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key,String.valueOf(userId));
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    //移除
    public long remove(String key,Integer userId) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key,String.valueOf(userId));
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    //统计
    public long count(String key) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    //判断状态
    public boolean status(String key,Integer userId) throws Exception{
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key,String.valueOf(userId));
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }


}
