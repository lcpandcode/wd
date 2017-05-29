package cn.cpliang.wenda.controllor;


import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/7.
 */
@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})
    public String registerLogin(){
        return "login";
    }

    /**
     *
     * @param model
     * @param userName
     * @param remember 是否记住登录状态，默认登录有效时间是一天，可以通过设置UserService的TICKET_AGE常量来设置登录时间
     * @param password
     * @param next
     * @param response
     * @return
     */
    @RequestMapping(path={"/login/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String login(Model model,
                        @RequestParam("username") String userName,
                        @RequestParam(value = "remember",defaultValue = "false") boolean remember,
                        @RequestParam("password") String password,
                        @RequestParam(value = "next" ,defaultValue = "/index") String next,
                        HttpServletResponse response){

        Map<String,Object> map = userService.login(userName,password);
        if(map.get("msg")!=null){
            model.addAttribute("msg",map.get("msg").toString());
            return "login";
        }
        Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
        cookie.setPath("/");//cookie对所有页面均有效
        if(remember){
            cookie.setMaxAge(userService.TICKET_AGE);//设置浏览器保存cookie，有效时间为TICKET_AGE
        }
        response.addCookie(cookie);
        return "redirect:" + next;
    }

    /**
     *
     * @param model 模型，渲染模板用
     * @param userName 用户名，注册必须要有
     * @param password 用户密码
     * @param email 邮箱，需要用来发送激活邮件
     * @param next 表示注册前访问的页面，注册成功后为了提高用户体验返回这个页面
     * @param response
     * @return 注册成功，返回注册前的页面模板，否则返回注册页面重新注册
     */
    @RequestMapping(path={"/register/"},method = {RequestMethod.POST,RequestMethod.GET})
    public String register(Model model,
                           @RequestParam(value = "username",required = false) String userName,
                           @RequestParam(value = "password",required = false) String password,
                           @RequestParam(value = "email",required = false) String email,
                           @RequestParam(value = "next" ,defaultValue = "/index") String next,
                           HttpServletResponse response){
        if(userName==null || password==null || email==null){
            //无数据，表明是访问注册页面，重定向到注册页面
            return "register";
        }
        Map<String,Object> map = userService.register(userName,password,email);
        if(map.get("msg")!=null){
            model.addAttribute("msg",map.get("msg"));
            return "register";
        }
        Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
        cookie.setPath("/");//cookie对所有页面均有效
        response.addCookie(cookie);
        //成功，重定向到注册前访问页面
        return "redirect:" + next;
    }

    @RequestMapping(path={"/active"},method = {RequestMethod.POST,RequestMethod.GET})
    public String active(@RequestParam("activeId") String activeId,
                       @RequestParam("userId") String userId,
                       Model model){
        //验证activeId是否正确
        boolean result = userService.active(activeId,userId);
        if(result){
            model.addAttribute("activeMsg","激活成功");
        }else{
            model.addAttribute("activeMsg","激活地址无效，激活失败 ");
        }
        return "login";
    }

}
