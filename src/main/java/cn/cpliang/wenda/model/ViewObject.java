package cn.cpliang.wenda.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/6.
 */
public class ViewObject {
    Map<String,Object> objs = new HashMap<>();

    public void set(String key,Object value){
        objs.put(key,value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
