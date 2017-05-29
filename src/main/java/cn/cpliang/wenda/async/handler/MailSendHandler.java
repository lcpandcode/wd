package cn.cpliang.wenda.async.handler;

import cn.cpliang.wenda.async.EventHandler;
import cn.cpliang.wenda.async.EventModel;
import cn.cpliang.wenda.async.EventType;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/16.
 */
public class MailSendHandler implements EventHandler {
    @Override
    public void doHandler(EventModel model) {
        //调用邮件发送函数

    }

    @Override
    public List<EventType> getHandlerType() {
        return null;
    }
}
