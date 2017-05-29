package cn.cpliang.wenda.interceptor;

import cn.cpliang.wenda.dao.TicketDAO;
import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.LoginTicket;
import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.service.TicketService;
import cn.cpliang.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by lcplcp on 2017/5/8.
 */
@Component
public class PassportIntercerptor implements HandlerInterceptor{
    @Autowired
    UserService userService;
    @Autowired
    TicketService ticketService;
    @Autowired
    HostHolder hostHolder;
    @Override
    //这里的布尔值表示是否继续执行请求链，如果返回false，表示interceptor已经处理该请求
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //进行请求服务前要进行登录身份验证
        String ticket = null;
        //1、获取cookie
        Cookie [] cookies = httpServletRequest.getCookies();
        if(cookies==null){
            return true;
        }
        //2、解析cookie，查看是否还有ticket
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("ticket")){
                ticket = cookie.getValue();
                break;
            }
        }

        //3、判断ticket是否有效
        LoginTicket loginTicket = null;
        if(ticket!=null){
            loginTicket = ticketService.getLoginTicketByTicket(ticket);
            if(loginTicket!=null && loginTicket.getExpired().after(new Date()) && loginTicket.getStatus()==0){
                //获取用户的数据
                User user = userService.getUserById(loginTicket.getUserId());
                //set进hostHolder
                hostHolder = new HostHolder();
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if(hostHolder.getUser()!=null && modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清除操作
        hostHolder.clearUser();
    }
}
