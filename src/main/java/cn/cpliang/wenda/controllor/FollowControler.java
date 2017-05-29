package cn.cpliang.wenda.controllor;

import cn.cpliang.wenda.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by lcplcp on 2017/5/20.
 */
@Controller
public class FollowControler {
    @Autowired
    FollowService followService;


}
