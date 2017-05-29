package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.async.EventModel;
import cn.cpliang.wenda.async.EventProducer;
import cn.cpliang.wenda.async.EventType;
import cn.cpliang.wenda.model.Comment;
import cn.cpliang.wenda.model.EntityType;
import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.User;
import cn.cpliang.wenda.service.CommentService;
import cn.cpliang.wenda.service.LikeService;
import cn.cpliang.wenda.service.UserService;
import cn.cpliang.wenda.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by lcplcp on 2017/5/12.
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    //新增评论的赞
    @RequestMapping(path={"/like/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addCommentLike(@RequestParam("commentId") int commentId){
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin";
        }
        long count = 0;
        try {
            //进行点赞操作
            likeService.addLike(user.getId(), EntityType.ENTITY_COMMENT,commentId);
            //根据id读取评论
            Comment comment = commentService.getCommentById(commentId);
            //进行邮件发送操作（将邮件发送事件推到handler队列里面）
            //点赞之前，需要进行邮件发送
            EventModel model = new EventModel();
            model.setEventType(EventType.LIKE);
            model.setEntityId(commentId);
            model.setEntityType(EntityType.ENTITY_COMMENT);
            model.setActionOwnerId(comment.getUserId());
            model.setExts("date",new Date().toString());
            model.setExts("email",user.getUserEmail());
            eventProducer.add(model);
            //获取赞的人数
            count = likeService.likeCount(EntityType.ENTITY_COMMENT,commentId);
        } catch (Exception e) {
            logger.error("点赞失败："+e.getMessage());
            return jsonUtil.getJson(5,"点赞失败");
        }
        return jsonUtil.getJson(0,String.valueOf(count));
    }

    //删除评论的赞
    @RequestMapping(path={"/like/delete"},method = {RequestMethod.POST})
    @ResponseBody
    public String removeCommentLike(@RequestParam("commentId") int commentId){
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin";
        }
        long count = 0;
        try {
            likeService.removeLike(user.getId(), EntityType.ENTITY_COMMENT,commentId);
            count = likeService.likeCount(EntityType.ENTITY_COMMENT,commentId);
        } catch (Exception e) {
            logger.error("移除点赞失败："+e.getMessage());
            return jsonUtil.getJson(5,"移除点赞失败");
        }
        return jsonUtil.getJson(0,String.valueOf(count));
    }

    //新增评论的踩（注意同时提出赞）
    @RequestMapping(path={"/dislike/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addCommentDislike(@RequestParam("commentId") int commentId){
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin";
        }
        long count = 0;
        try {
            likeService.removeDislike(user.getId(), EntityType.ENTITY_COMMENT,commentId);
            count = likeService.likeCount(EntityType.ENTITY_COMMENT,commentId);
        } catch (Exception e) {
            logger.error("移除点赞失败："+e.getMessage());
            return jsonUtil.getJson(5,"移除点赞失败");
        }
        return jsonUtil.getJson(0,String.valueOf(count));
    }

    //删除评论的踩
    @RequestMapping(path={"/dislike/delete"},method = {RequestMethod.POST})
    @ResponseBody
    public String removeCommentDislike(@RequestParam("commentId") int commentId){
        User user = hostHolder.getUser();
        if(user==null){
            return "redirect:/reglogin";
        }
        long count = 0;
        try {
            likeService.removeDislike(user.getId(), EntityType.ENTITY_COMMENT,commentId);
            count = likeService.likeCount(EntityType.ENTITY_COMMENT,commentId);
        } catch (Exception e) {
            logger.error("移除点赞失败："+e.getMessage());
            return jsonUtil.getJson(5,"移除点赞失败");
        }
        return jsonUtil.getJson(0,String.valueOf(count));
    }

}
