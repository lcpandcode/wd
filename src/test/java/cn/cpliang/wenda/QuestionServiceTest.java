package cn.cpliang.wenda;

import cn.cpliang.wenda.service.QuestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by lcplcp on 2017/5/9.
 */
@SpringApplicationConfiguration(classes=WendaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class QuestionServiceTest {
    @Autowired
    QuestionService questionService;
    @Test
    public void addQuestionTest(){
        Map map = questionService.addQuestion("content1","title11111");
        //System.out.println(map.get("msg"));
    }
}
