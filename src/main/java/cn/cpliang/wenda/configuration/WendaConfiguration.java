package cn.cpliang.wenda.configuration;

import cn.cpliang.wenda.interceptor.LoginInteceptor;
import cn.cpliang.wenda.interceptor.PassportIntercerptor;
import cn.cpliang.wenda.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lcplcp on 2017/5/8.
 */
@Component
public class WendaConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassportIntercerptor passportIntercerptor;
    @Autowired
    LoginInteceptor loginInteceptor;
    /*
    说明：
    重写父类的addinterceptors，将拦截器注册到拦截器链路上
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercerptor);//说明：其实底层实现就是add进一个list
        registry.addInterceptor(loginInteceptor).addPathPatterns("/user/*");//登录拦截
        super.addInterceptors(registry);
    }
}
