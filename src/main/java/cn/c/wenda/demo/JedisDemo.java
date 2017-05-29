package cn.c.wenda.demo;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lcplcp on 2017/5/17.
 */
public class JedisDemo {
    public static void main(String [] args){
        Jedis jedis = new Jedis("redis://localhost:6379/1");
        jedis.hset("mapTest1","key1","value1");
        String test = jedis.hget("mapTest1","key1");
        System.out.println(test);

        jedis.zadd("zsetTest1",3,"val1");
        jedis.zadd("zsetTest1",2,"val2");
        jedis.zadd("zsetTest1",2,"val3");
        jedis.zadd("zsetTest2",1,"val1");
        jedis.zadd("zsetTest2",2,"val2");

        jedis.zinterstore("dest","zsetTest1","zsetTest1");
        List<String> list = setToList(jedis.zrangeByLex("dest","-","+"));
        for(String str:list){
            System.out.println(str);
        }


    }
    private static List<String> setToList(Set<String> set){
        List<String> list = new ArrayList<>();
        for(String str : set){
            list.add(str);
        }
        return list;
    }
}
