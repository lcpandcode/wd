package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.dao.MessageDAO;
import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.Message;
import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.model.ViewObject;
import cn.cpliang.wenda.service.MessageService;
import cn.cpliang.wenda.service.UserService;
import cn.cpliang.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/9.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    JsonUtil jsonUtil;


    //根据用户id获取未读消息列表
    @RequestMapping(path={"/msg/list"},method = {RequestMethod.GET})
    public String messageList(Model model){
        User user = hostHolder.getUser();
        if(user==null){

            return "redirect:/reglogin";
        }
        List<Message> messages = null;
        try {
            messages = messageService.selectMessagesListByUserId(user.getId(),0,10);
        } catch (Exception e) {
            logger.error("读取用户消息列表出错：" + e.getMessage());
            e.printStackTrace();
        }
        List<ViewObject> vos = new ArrayList<>();
        ViewObject conversation = null;
        int target;
        for(Message m:messages){
            conversation = new ViewObject();
            conversation.set("message",m);
            target = (user.getId()==m.getFromId()?m.getToId():m.getFromId());
            conversation.set("user", userService.getUserById(target));
            //统计回话未读消息
            conversation.set("unreadCount",messageService.countUnreadMessages(m.getConversationId()));
            vos.add(conversation);
        }
        model.addAttribute("conversations",vos);
        return "letter";
    }

    @RequestMapping(path={"/msg/{conversationId}"},method = {RequestMethod.GET})
    public String messagesDetail(@PathVariable("conversationId") String conversationId,
                                 Model model){
        //String conversationId = "2_15";
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin";
        }
        //验证conversationId中是否包含用户id，防止非法访问数据
        String [] conversation = conversationId.split("_");
        if(conversation.length!=2 ||
                (Integer.valueOf(conversation[0])!=user.getId() && Integer.valueOf(conversation[1])!=user.getId())){
            model.addAttribute("msg","回话id非法！回话id必须包含用户id");
            return "/error";
        }
        //根据conversationId读取回话记录
        List<Message> messages = null;
        try {
            messages = messageService.selectMessagesByConversationId(conversationId,0,10);
        } catch (Exception e) {
            logger.error("读取回话消息失败：" + e.getMessage());
            e.printStackTrace();
        }
        List<ViewObject> vos = new ArrayList<>();
        ViewObject vo ;
        for(Message m:messages){
            vo = new ViewObject();
            vo.set("message",m);
            vo.set("user",userService.getUserById(m.getFromId()));
            vos.add(vo);
        }
        model.addAttribute("messages",vos);
        return "letterDetail";

    }

    //增加私信
    @RequestMapping(path={"/msg/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content){
        User user = hostHolder.getUser();
        if(user==null){
            return jsonUtil.getJson(999,"用户未登录");
        }
        //根据用户名查找用户id
        User toUser = userService.getUserByName(toName);
        if(toUser==null){
            return jsonUtil.getJson(3,"用户名不存在");
        }
        int toId = toUser.getId();
        int fromId = user.getId();
        String conversationId = String.format("%d_%d",toId>fromId?fromId:toId,toId>fromId?toId:fromId);
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setCreatedDate(new Date());
        message.setFromId(user.getId());
        message.setToId(toUser.getId());
        message.setHasRead(0);
        message.setConversationId(conversationId);
        message.setContent(content);
        Map<String,Object> map = null;
        try{
           map = messageService.addMessage(message);
        }catch (Exception e){
            logger.error("新增消息失败：" + e.getMessage());
            e.printStackTrace();
        }
        if(map.get("msg")!=null) {
            return jsonUtil.getJson(4, map.get("msg").toString());
        }
        return jsonUtil.getJson(0,"添加消息成功");
    }
}
