package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.model.*;
import cn.cpliang.wenda.service.FollowService;
import cn.cpliang.wenda.service.QuestionService;
import cn.cpliang.wenda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.View;
import java.util.*;

/**
 * Created by lcplcp on 2017/5/4.
 */
@Controller
public class IndexController {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @RequestMapping(path={"/index","/"},method = {RequestMethod.GET})
    public String index(Model model){
        model.addAttribute("vos",getQuestions(0,0,11));
        //读取关注相关信息

        return "index";
    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET})
    public String userIndex(Model model,@PathVariable("userId") int userId){
        model.addAttribute("vos",getQuestions(userId,0,11));
        //1、读取关注人总数
        long follloweeCount = followService.coutFollowee(userId, EntityType.ENTITY_USER);
        model.addAttribute("followeeCount",follloweeCount);
        //2、获取关注人列表
        List<String> followees = followService.getFolloweeList(userId,EntityType.ENTITY_USER,0,8);
        List<ViewObject> users = new ArrayList<>();
        for(String str:followees){
            //根据id查找用户信息
            ViewObject vo = new ViewObject();
            User user = userService.getUserById(Integer.parseInt(str));
            vo.set("user",user);
            //读取该用户的粉丝数目
            long followerCount = followService.coutFollower(EntityType.ENTITY_USER,user.getId());
            vo.set("followerCount",followerCount);
            //后面待完善
            //统计回答数目
            //统计赞同数目
            //统计提问数目
            //统计赞个数
            users.add(vo);
        }
        ModelAndView modelAndView = new ModelAndView();
        //modelAndView.
        return "index";
    }

    private List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<ViewObject> objsList = new ArrayList<>() ;
        List<Question> questionList = questionService.selectLatestQuestion(userId,offset,limit);
        ViewObject obj = null;
        for(Question question:questionList){
            obj = new ViewObject();
            question.setCreatedDate(question.getCreatedDate());
            obj.set("question",question);
            obj.set("user",userService.getUserById(question.getUserId()));
            objsList.add(obj);
        }
        return objsList;
    }

    @RequestMapping(path={"/logout"},method = {RequestMethod.GET})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/reglogin";
    }

    @RequestMapping(path={"/test/modelView"},method = {RequestMethod.GET})
    //@ResponseBody
    public ModelAndView tt(){
        ModelAndView modelAndView = new ModelAndView("modelView");
        modelAndView.addObject("testf","val1");
        return modelAndView;
        //model.addAttribute("test1","val2");
        //return "modelView";

    }
}
