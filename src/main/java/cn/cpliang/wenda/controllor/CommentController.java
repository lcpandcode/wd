package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.model.*;
import cn.cpliang.wenda.service.CommentService;
import cn.cpliang.wenda.service.QuestionService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;


/**
 * Created by lcplcp on 2017/5/9.
 */
@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path={"/comment/question/{questionId}"},method = {RequestMethod.POST})
    public String addQuestionComment(@PathVariable("questionId") int questionId,
                                     @RequestParam("content") String content,
                                     Model model){
        //查看用户是否登录
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin/";
        }
        //查看question id是否存在
        Question question = questionService.getQuestionById(questionId);
        if(question==null){
            return "redirect:/error";
        }
        Comment comment = new Comment();
        comment.setStatus(0);
        comment.setEntityType(EntityType.ENTITY_QUESTION);
        comment.setCreateDate(new Date());
        comment.setEntityId(questionId);
        comment.setContent(content);
        comment.setUserId(user.getId());
        Map<String,Object> map = commentService.addComment(comment);

        //同时需要更新该问题的评论数目(这里应该加个事务）
        int count = commentService.countCommentByEntity(EntityType.ENTITY_QUESTION,questionId);
        questionService.updateCommentCountById(count+1,questionId);
        if(map.get("msg")!=null){
            //有错误信息，返回前端
            model.addAttribute("msg",map.get("msg"));
            return "redirect:/error";
        }
        return "redirect:/question/" + questionId;
    }



}
