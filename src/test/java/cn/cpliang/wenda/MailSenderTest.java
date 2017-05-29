package cn.cpliang.wenda;

import cn.cpliang.wenda.util.MailSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by lcplcp on 2017/5/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class MailSenderTest {
    @Autowired
    MailSender mailSender;

    @Test
    public void sendTest(){
        mailSender.sendWithHTMLTemplate("1140823948@qq.com",
                "测试主题","mail.html",new HashMap<String, Object>());
    }
}
