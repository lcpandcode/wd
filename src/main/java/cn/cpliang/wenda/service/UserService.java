package cn.cpliang.wenda.service;

import cn.cpliang.wenda.async.EventModel;
import cn.cpliang.wenda.async.EventProducer;
import cn.cpliang.wenda.async.EventType;
import cn.cpliang.wenda.dao.TicketDAO;
import cn.cpliang.wenda.dao.UserDAO;
import cn.cpliang.wenda.model.LoginTicket;
import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.util.JedisActive;
import cn.cpliang.wenda.util.JedisUtil;
import cn.cpliang.wenda.util.WendaUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lcplcp on 2017/5/6.
 */
@Service
public class UserService {
    private static String DEFAULT_HEAD = "/images/res/default.jpg";
    public int TICKET_AGE = 3600*24*1000;//登录的ticket有效期
    public static int PASS_LENGTH_LIMIT = 6;
    @Autowired
    UserDAO userDAO;
    @Autowired
    TicketDAO ticketDAO;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    JedisUtil jedisUtil;
    @Autowired
    JedisActive jedisActive;
    public User getUserById(int id){
        return userDAO.selectUserById(id);
    }


   public Map<String,Object> login(String userName,String password){
        Map<String,Object> map = new HashMap<>();
        //验证操作
        if(StringUtils.isBlank(userName)){
            map.put("msg","用户名不能为空");
            return map;
        }else if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDAO.getUserByName(userName);
        if(user==null){
            map.put("msg","用户名不存在");
            return map;
        }
        //验证密码是佛正确
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
           map.put("msg","密码错误");
           return map;
       }
        //用户是否激活
       if(user.getIsActive()==1){
            map.put("msg","用户尚未激活，登录邮箱进行激活操作");
            return map;
       }
        //返回tricket
        String ticket = addTicket(user.getId());
        map.put("ticket",ticket);
        map.put("user",user);
        return map;
    }

    private String addTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        //设置有效日期
        Date date = new Date();
        date.setTime(date.getTime()+TICKET_AGE);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        ticketDAO.addTicket(ticket);
        return ticket.getTicket();
    }

    //注册功能
    public Map<String,Object> register(String name,String password,String email){
        Map<String,Object> map = new HashMap<>();
        //字段合法性验证
        //1、是否为空验证
        if(StringUtils.isBlank(name)){
            map.put("msg","用户名不能为空");
            return map;
        }else if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }else if(StringUtils.isBlank(email)){
            //注意要加上邮箱验证，待改进
            map.put("msg","邮箱填写不正确");
            return map;
        }
        //2、非法字符过滤

        //3、密码复杂度检验
        if(StringUtils.length(password)<PASS_LENGTH_LIMIT){
            map.put("msg","密码长度至少六位");
            return map;
        }
        //用户名是否被注册验证
        User user = userDAO.getUserByName(name);
        if(user!=null){
            map.put("msg","用户已被注册");
            return map;
        }
        //通过验证，进行注册操作
        user = new User();
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String passwordMd5 = WendaUtil.MD5(password + user.getSalt());
        user.setPassword(passwordMd5);
        user.setHeadUrl(DEFAULT_HEAD);
        user.setName(name);
        user.setUserEmail(email);

        //1、写入数据库
        userDAO.addUser(user);
        //2、发邮件
        EventModel registerEventModel = new EventModel();
        registerEventModel.setExts("email",email);
        registerEventModel.setEventType(EventType.REGISTER_ACTIVE);
        registerEventModel.setActionId(user.getId());
        eventProducer.add(registerEventModel);
        //注册成功进行登录操作
        //1、add ticket
        String ticket = addTicket(user.getId());
        //2、put and return ticket
        map.put("ticket",ticket);
        map.put("user",user);
        return map;
    }

    //退出方法，修改status
    public int logout(String ticket){
       return ticketDAO.update(1,ticket);
    }

    //根据用户名查找用户
    public User getUserByName(String name){
        return userDAO.getUserByName(name);
    }

    //激活操作
    public boolean active(String activeId,String userId){
        boolean rtn = false;
        boolean result = jedisActive.active(activeId,userId);
        //激活成功，修改激活状态
        if(result){
            rtn = userDAO.active(userId);
        }
        return rtn;
    }


}
