package cn.cpliang.wenda;

import cn.cpliang.wenda.dao.TicketDAO;
import cn.cpliang.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lcplcp on 2017/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class TicketDAOTest {
    @Autowired
    TicketDAO ticketDAO;
    @Test
    public void updateTest(){
        int rtn = ticketDAO.update(1,"72bac2d09fd84077a90302b51df978a6");
        System.out.println("rtn=" + rtn);
    }
    @Test
    public void deleteTest(){
        int rtn = ticketDAO.delete("72bac2d09fd84077a90302b51df978a6");
        System.out.println("rtn=" + rtn);
    }
    @Test
    public void selectTest(){
        LoginTicket ticket = ticketDAO.selectLoginTicket("72bac2d09fd84077a90302b51df978a6");
        System.out.println("ticket=" + ticket);
    }

}
