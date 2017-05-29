package cn.cpliang.wenda;

import cn.cpliang.wenda.dao.MessageDAO;
import cn.cpliang.wenda.model.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by lcplcp on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class MessageDAOTest {
    @Autowired
    MessageDAO messageDAO;
    //插入方法测试
    @Test
    public void addMessageTest(){
        Message message = null;
        for(int i=0;i<4;i++){
            message = new Message();
            message.setContent(String.format("content%d" ,i));
            message.setConversationId("2_3");
            message.setCreatedDate(new Date());
            message.setFromId(2);
            message.setToId(3);
            message.setHasRead(0);
            messageDAO.addMessage(message);
        }
    }
    @Test
    public void selectMessageByIdTest(){
        Message message = messageDAO.selectMessageById(1);
        System.out.println(message.getContent());
    }
    @Test
    public void selectMessageByConversationIdTest(){
        List<Message> messages = messageDAO.selectMessageByConversationId("2_3",1,2);
        for(Message m:messages){
            System.out.println(m.getContent());
        }
    }
    @Test
    public void selectUnreadMessageByToIdTest(){
        List<Message> messages = messageDAO.selectUnreadMessageByToId(3,1,3);
        for(Message m:messages){
            System.out.println(m.getContent());
        }
    }
    @Test
    public void countUnRead(){
        System.out.println(messageDAO.countUnreadMessage(3,"2_3"));
    }
    @Test
    public void updateStatus(){
        messageDAO.updateMessageStatusById(1,1);
    }
    @Test
    public void selectMessagesListTest(){
        List<Message> messages = messageDAO.selectMessagesListByUserId(1,0,3);
        for(Message m:messages){
            System.out.println(m.getContent());
        }
    }
    @Test
    public void updateMessageStatusByUserIdTest(){
        messageDAO.updateMessageStatusByUserId("2_3",3,1);
    }
}
