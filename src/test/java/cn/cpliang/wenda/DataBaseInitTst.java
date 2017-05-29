package cn.cpliang.wenda;

import cn.cpliang.wenda.dao.QuestionDAO;
import cn.cpliang.wenda.dao.UserDAO;
import cn.cpliang.wenda.model.Question;
import cn.cpliang.wenda.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lcplcp on 2017/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
//@Sql("/init-schema.sql")
public class DataBaseInitTst {
    @Autowired
   UserDAO userDAO;
    @Autowired
    QuestionDAO questionDAO;
    @Test
    public void contextLoads() {
        String imageUrl = "/images/res/head";
        User user = new User();
        for(int i=1;i<11;i++){
            user.setPassword(String.format("pass%d",i));
            user.setName(String.format("name%d",i));
            user.setHeadUrl(String.format("/images/res/head%d.jpg",i));
            user.setSalt(String.format("salt%d",i));
            userDAO.addUser(user);
        }
        Question question = new Question();
        for(int i=1;i<11;i++){
            question.setCommentCount(i);
            question.setContent(String.format("content%d",i));
            //question.setCreateDate(new Date());
            question.setCreatedDate(new Date());
            question.setTitle(String.format("title%d",i));
            question.setUserId(i);
            questionDAO.addQuestion(question);
        }
        System.out.println(questionDAO.selectLatestQuestions(0,0,10).size());

    }
    @Test
    public void selectTest(){
        User user = null;
        for(int i=1;i<11;i++){
            user = userDAO.selectUserById(i);
            System.out.println(user.toString());
        }
    }
    /*
    @Test
    public void selectQUestionsTest(){
        List<Question> list = questionDAO.selectLastestQuestion(0,1,5);
        for(Question q:list){
            System.out.println(q.toString());
        }
    }
    */
}
