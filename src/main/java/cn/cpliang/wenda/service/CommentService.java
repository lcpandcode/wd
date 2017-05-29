package cn.cpliang.wenda.service;

import cn.cpliang.wenda.dao.CommentDAO;
import cn.cpliang.wenda.model.Comment;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/9.
 */
@Service
public class CommentService {
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    CommentDAO commentDAO;
    //增加评论
    public Map<String,Object> addComment(Comment comment){
        Map<String,Object> map = new HashMap<>();
        //进行合法性检验
        String content = comment.getContent();
        if(StringUtils.isBlank(content)){
            map.put("msg","评论内容不能为空");
            return map;
        }
        //进行非法字符过滤
        content =  sensitiveService.filter(content);
        content = HtmlUtils.htmlEscape(content);
        comment.setContent(content);
        //写入数据库
        commentDAO.addComment(comment);
        return map;
    }

    //获取评论列表
    public List<Comment> getCommentByEntity(int entityType, int entityId,
                                            int offset,int limit){
        return commentDAO.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    //统计评论条数
    public int countCommentByEntity(int entityType,int entityId){
        return commentDAO.countComment(entityType,entityId);
    }

    //根据id获取评论具体信息
    public Comment getCommentById(int id){
        return commentDAO.selectCommentById(id);
    }
}
