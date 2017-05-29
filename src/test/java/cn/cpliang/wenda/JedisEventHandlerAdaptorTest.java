package cn.cpliang.wenda;

import cn.cpliang.wenda.util.JedisEventHandlerAdaptor;
import cn.cpliang.wenda.util.JedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class JedisEventHandlerAdaptorTest {
    @Autowired
    JedisEventHandlerAdaptor jedisEventHandlerAdaptor;
    @Autowired
    JedisKeyUtil jedisKeyUtil;

    @Test
    public void popTest(){
        jedisEventHandlerAdaptor.add(jedisKeyUtil.getEventHandlerKey(),"test");
        List<String> strs = jedisEventHandlerAdaptor.pop("10000",jedisKeyUtil.getEventHandlerKey());
        System.out.println(strs.get(0));
    }
}
