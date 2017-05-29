package cn.cpliang.wenda;

import cn.cpliang.wenda.service.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes=WendaApplication.class)
public class FollowServiceTest {
    @Autowired
    FollowService followService;

    @Test
    public void test1(){
        followService.follow(1,1,2);
        followService.follow(1,1,3);
        System.out.println(followService.coutFollower(1,1));

        followService.unfollow(1,1,2);
        List<String> list_follower = followService.getFollowerList(1,1,0,2);

        for(String str:list_follower){
            System.out.println(str);
        }
        System.out.println("////////////////");
        List<String> list_followee = followService.getFolloweeList(2,1,0,10);
        for(String str:list_followee){
            System.out.println(str);
        }

        System.out.println(followService.coutFollowee(2,1));
        //System.out.println(followService.coutFollower(1,1));
    }

}
