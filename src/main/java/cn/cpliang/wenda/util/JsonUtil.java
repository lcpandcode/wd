package cn.cpliang.wenda.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import sun.management.MappedMXBeanType;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/9.
 */
@Component
public class JsonUtil {
    //根据字符创建json
    public String getJson(int code,String msg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return  jsonObject.toJSONString();
    }
    public String getJson(Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        for(Map.Entry<String,Object> entry:map.entrySet()){
            jsonObject.put(entry.getKey(),entry.getValue());
        }
        return  jsonObject.toJSONString();
    }
}
