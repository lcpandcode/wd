package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.model.*;
import cn.cpliang.wenda.service.CommentService;
import cn.cpliang.wenda.service.LikeService;
import cn.cpliang.wenda.service.QuestionService;
import cn.cpliang.wenda.service.UserService;
import cn.cpliang.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/9.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @RequestMapping(path={"/question/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String questionAdd(@RequestParam("title") String title,
                              @RequestParam("content") String content){
        Question question = new Question();
        Map<String,Object> map = questionService.addQuestion(title,content);
        if(map.get("msg")!=null){
            return jsonUtil.getJson(map);
        }
        if(hostHolder.getUser()==null){
            return jsonUtil.getJson(999,"用户尚未登录");
        }
        return jsonUtil.getJson(0,"发布成功");
    }

    //查看问题详情
    @RequestMapping(path={"/question/{questionId}"},method = {RequestMethod.GET})
    public String questionDetail(@PathVariable("questionId") int questionId,
                                 Model model){
        User user = hostHolder.getUser();
        if(user== null){
            return "redirect:/reglogin";
        }

        //读取questionId对应的评论数据数据
        Question question = questionService.getQuestionById(questionId);
        //获取问题的评论列表
        List<Comment> comments = commentService.getCommentByEntity(EntityType.ENTITY_QUESTION,questionId,0,10);
        List<ViewObject> vos = new ArrayList<>();
        ViewObject vo = null;
        for(Comment c:comments){
            vo = new ViewObject();
            vo.set("comment",c);
            int liked = 0;
            long likedCount = 0;
            try {
                //查看用户对该条评论的状态
                if(likeService.isLikeByUserId(user.getId(),EntityType.ENTITY_COMMENT,c.getId())){
                    liked = 1;
                }else if(likeService.isDislikeByUserId(user.getId(),EntityType.ENTITY_COMMENT,c.getId())){
                    liked = -1;
                }
                //获取评论赞人数
                likedCount = likeService.likeCount(EntityType.ENTITY_COMMENT,c.getId());
            } catch (Exception e) {
                logger.error("读取评用户评论状态错误：" + e.getMessage());
            }
            vo.set("liked",liked);
            vo.set("likeCount",likedCount);
            vo.set("user",userService.getUserById(c.getUserId()));
            vos.add(vo);
        }
        //获取评论总数
        //int count = commentService.countCommentByEntity(EntityType.ENTITY_QUESTION,questionId);
        //model.addAttribute("count",count);
        model.addAttribute("question",question);
        model.addAttribute("comments",vos);
        return "detail";
    }
}
