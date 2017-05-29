package cn.cpliang.wenda.service;

import cn.cpliang.wenda.dao.MessageDAO;
import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/10.
 */
@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;
    //根据用户id读取用户未读消息列表
    public List<Message> selectMessagesListByUserId(int userId,int offset,int limit) throws  Exception{
        return messageDAO.selectMessagesListByUserId(userId,offset,limit);
    }

    //根据用户id以及发送者id统计未读消息条数
    public int countUnreadMessages(String conversationId){
        return messageDAO.countUnreadMessage(hostHolder.getUser().getId(),conversationId);
    }

    //根据conversationId读取私聊记录
    public List<Message> selectMessagesByConversationId  (String conversationId,int offset,int limit) throws  Exception{
        //修改消息是否已读的状态，这里应该放在同一个事务里面
        if(hostHolder.getUser()==null){
           throw new RuntimeException("无法找到用户信息，用户未曾登录");
        }
        int userId = hostHolder.getUser().getId();
        int result = messageDAO.updateMessageStatusByUserId(conversationId,userId,1);
        if(result<0){
            throw new RuntimeException("更新消息读取状态时数据库操作有误，更新失败");
        }
        return messageDAO.selectMessageByConversationId(conversationId, offset,limit);
    }

    //修改消息是否被读状态
    public int updateMessageHas(int messageId,int status) throws  Exception{
        return messageDAO.updateMessageStatusById(messageId,status);
    }

    //新增消息
    public Map<String,Object> addMessage(Message message) throws Exception{
        Map<String,Object> rtn = new HashMap<>();
        //进行合法性验证
        String content = message.getContent();
        //是否是空字符
        if(content==null || StringUtils.isBlank(content)){
            rtn.put("msg","消息内容不能为空");
            return rtn;
        }
        //过滤敏感字符
        content = sensitiveService.filter(content);
        //过滤html
        content = HtmlUtils.htmlEscape(content);
        message.setContent(content);
        int resultAdd = messageDAO.addMessage(message);
        if(resultAdd<0) {
            rtn.put("msg","系统错误");
        }
        return rtn;
    }
}
