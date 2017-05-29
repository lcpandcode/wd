package cn.cpliang.wenda.service;

import cn.cpliang.wenda.dao.TicketDAO;
import cn.cpliang.wenda.model.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcplcp on 2017/5/8.
 */
@Service
public class TicketService {
    @Autowired
    TicketDAO ticketDAO;
    public LoginTicket getLoginTicketByTicket(String ticket){
        return ticketDAO.selectLoginTicket(ticket);
    }
}
