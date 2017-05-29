package cn.cpliang.wenda.async.handler;

import cn.cpliang.wenda.async.EventHandler;
import cn.cpliang.wenda.async.EventModel;
import cn.cpliang.wenda.async.EventType;
import cn.cpliang.wenda.util.JedisActive;
import cn.cpliang.wenda.util.MailSender;
import cn.cpliang.wenda.util.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lcplcp on 2017/5/16.
 */
@Component
public class ActiveHandler implements EventHandler {
    private static final String ACTIVE_URL = "/active";
    private static final String ACTIVE_SUBJECT = "激活邮件";
    private static final String ACTIVE_TEMPLETE = "registerActive.html";
    private static final String ACTIVE_PRARM = "?userId=%s&activeId=%s";
    @Autowired
    private MailSender mailSender;
    @Autowired
    private JedisActive jedisActive;
    @Override
    public void doHandler(EventModel model) {
        //生成激活url，并写进redis
        StringBuilder url = new StringBuilder(ServerConfig.getHOST());
        String activeId = UUID.randomUUID().toString();
        url.append(ACTIVE_URL);//?userId=2&activeId=3
        url.append(String.format(ACTIVE_PRARM,model.getActionId(),activeId));
        //将activeId存储在redis
        jedisActive.put(activeId,String.valueOf(model.getActionId()));
        Map<String,Object> modelActive = new HashMap<>();
        modelActive.put("userName",model.getExts("userName"));
        modelActive.put("activeUrl",new String(url));
        System.out.println("///////////////" + model.getActionId() + "///" + model.getExts("userName"));
        mailSender.sendWithHTMLTemplate(model.getExts("email").toString(),ACTIVE_SUBJECT,ACTIVE_TEMPLETE,modelActive);
    }

    /**
     * 说明：激活handler只是在注册激活时用到，所以只有REGISTER_ACTIVE注册
     * @return
     */
    @Override
    public List<EventType> getHandlerType() {
        return Arrays.asList(EventType.REGISTER_ACTIVE);
    }
}
