package cn.cpliang.wenda;

import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.service.UserService;
import org.apache.commons.validator.Msg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by lcplcp on 2017/5/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void loginTest(){
        Map<String,Object> map =  userService.login("name2","pass2");
        System.out.println(map.get("msg"));
        System.out.println(map.get("ticket"));
        System.out.println(map.get("user"));
    }

    /*
    @Test
    public void registerTest(){
        Map<String,Object> map = userService.register("nameTest","passTest");
        System.out.println(map.get("msg"));
        System.out.println(map.get("ticket"));
    }
    */
}
