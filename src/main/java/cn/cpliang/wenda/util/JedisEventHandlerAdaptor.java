package cn.cpliang.wenda.util;

/**
 * Created by lcplcp on 2017/5/15.
 */

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 异步处理jedis连接工具类
 */
@Component
public class JedisEventHandlerAdaptor implements InitializingBean {
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    JedisKeyUtil jedisKeyUtil;
    JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool = jedisUtil.getJedisPool();
    }
    //add
    public void add(String key,String model){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            jedis.lpush(key,model);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }
    //事件pop
    public List<String> pop(String timeout,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.brpop(key,timeout);
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

}
