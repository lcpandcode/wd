package cn.cpliang.wenda.model;

import org.springframework.stereotype.Component;

/**
 * Created by lcplcp on 2017/5/8.
 */
@Component
//该类用于存储全局性的变量数据，注意使用完成要进行清除操作
public class HostHolder {
   private static ThreadLocal<User> users = new ThreadLocal<>();

   public User getUser(){
       return users.get();
   }
   public void setUser(User user){
       users.set(user);
   }
   public void clearUser(){
       users.remove();
   }
}
