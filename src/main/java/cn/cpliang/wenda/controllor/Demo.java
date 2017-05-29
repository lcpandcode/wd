package cn.cpliang.wenda.controllor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by lcplcp on 2017/5/4.
 */
//@Controller
public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @RequestMapping(path={"/index","/haha"})
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String demo3(@PathVariable("userId") int userId,
                          @PathVariable("groupId") int groupId,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key",defaultValue = "fddffe",required = false) String key){
        return String.format("test1 %d %d type:%d key:%s" ,userId,groupId,type,key);
    }

    @RequestMapping(path={"/tem"})
   // @ResponseBody
    public String demo2(Model model){
        return null;
    }

}
