package cn.cpliang.wenda.controllor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by lcplcp on 2017/5/4.
 */
//@Controller
public class IndexControllerDemo {
    @RequestMapping(path={"/index","/haha"})
    @ResponseBody
    public String index(){
        return "hello world";
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") int groupId,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key",defaultValue = "fddffe",required = false) String key){
        return String.format("test1 %d %d type:%d key:%s" ,userId,groupId,type,key);
    }

    @RequestMapping(path={"/tem"})
   // @ResponseBody
    public String tem(Model model){
        model.addAttribute("test1","hadcha");
        Map<String,String> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2gddffgg");
        model.addAttribute("map",map);
        List<String> list = Arrays.asList(new String [] {"aaa","bbb","ccc"});
        model.addAttribute("list",list);
        System.out.println("tes");
        return "test";
    }

}
