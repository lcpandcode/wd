package cn.cpliang.wenda.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;

/**
 * Created by lcplcp on 2017/5/15.
 */
@Component
public class JedisUtil implements InitializingBean{//定义redis连接池的最大连接数
    private static  int MAX_CONNECTION = 10;
    private static  String CONNECTION_ADDRESS = "redis://localhost:6379/1";
    private  JedisPool jedisPool;
    @Override
    public void afterPropertiesSet() throws Exception {
        this.jedisPool = new JedisPool(JedisUtil.CONNECTION_ADDRESS);
    }
    public JedisPool getJedisPool(){
        return this.jedisPool;
    }
}
