package cn.cpliang.wenda;

import cn.cpliang.wenda.dao.CommentDAO;
import cn.cpliang.wenda.model.Comment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by lcplcp on 2017/5/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class CommentDAOTest {
    @Autowired
    CommentDAO commentDAO;
    //初始化数据库
    @Test
    public void initDataBase(){
        Comment comment = null;
        for(int i=0;i<5;i++){
            comment = new Comment();
            comment.setContent(String.format("content%d",i));
            comment.setUserId(i);
            comment.setCreateDate(new Date());
            comment.setEntityId(i);
            comment.setEntityType(1);
            comment.setStatus(0);
            commentDAO.addComment(comment);
        }

    }

    @Test
    public void addCommentTest(){
        Comment comment = null;
        comment = new Comment();
        comment.setContent("contenttest");
        comment.setUserId(1);
        comment.setCreateDate(new Date());
        comment.setEntityId(1);
        comment.setEntityType(1);
        comment.setStatus(0);
        commentDAO.addComment(comment);
    }
    @Test
    public void selectTest(){
       List<Comment> list = commentDAO.selectCommentByEntity(1,1,1,4);
        for(Comment c:list){
            System.out.println(c.getContent());
        }
    }

    @Test
    public void updateStatusTest(){
        commentDAO.updateStatus(1,2);
    }
    @Test
    public void countTest(){
        int count = commentDAO.countComment(1,1);
        Assert.assertEquals(2,count);
    }
}
