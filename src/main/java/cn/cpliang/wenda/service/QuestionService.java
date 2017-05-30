package cn.cpliang.wenda.service;

import cn.cpliang.wenda.dao.QuestionDAO;
import cn.cpliang.wenda.model.HostHolder;
import cn.cpliang.wenda.model.Question;
import cn.cpliang.wenda.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/6.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;
    //如果用户没有登录，给个匿名用户id给他，默认匿名用户id为1
    private final int ANONYM_DEFAULT_ID = 1;

    public Question getQuestionById(int id) {
        return questionDAO.selectQuestionById(id);
    }

    public List<Question> selectLatestQuestion(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Map<String, Object> addQuestion(String title, String content) {
        Map<String, Object> map = new HashMap<>();
        int userId = ANONYM_DEFAULT_ID;
        if (hostHolder.getUser() != null) {
            userId = hostHolder.getUser().getId();
        }
        //特殊字符以及敏感字符过滤
        //1、判断是否为空，null
        if (content == null || title == null
                || StringUtils.isBlank(title) || StringUtils.isBlank(content)) {
            map.put("msg", "内容或标题不能为空");
            map.put("code", "1");//1代表内容非法
            return map;
        }
        //2、过滤特殊字符（js语句以及html）
        content = HtmlUtils.htmlEscape(content);
        title = HtmlUtils.htmlEscape(title);
        //3、过滤敏感词语
        content = sensitiveService.filter(content);
        title = sensitiveService.filter(title);
        Question question = new Question();
        question.setTitle(title);
        question.setContent(content);
        question.setCreatedDate(new Date());
        question.setUserId(userId);
        question.setCommentCount(0);
        questionDAO.addQuestion(question);
        return map;
    }


    //更新评论数目
    public int updateCommentCountById(int count, int questionId) {
        return questionDAO.updateCommentCountById(count, questionId);
    }

    //根据用户id统计用户提问次数
    public int countQuestionByUserId(int userId) {
        return questionDAO.countQuestionByUserId(userId);
    }


    public static void main(String[] arrgs) {
        System.out.println(StringUtils.isEmpty(null));
        System.out.println(StringUtils.isEmpty(""));
        String hehe = "<script>alert('haha')</script>";
        hehe = HtmlUtils.htmlEscape(hehe);
        System.out.println(hehe);
    }

}
