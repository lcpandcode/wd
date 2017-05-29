package cn.cpliang.wenda.util;

/**
 * Created by lcplcp on 2017/5/16.
 */

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送工具类
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private static final String SEND_NAME = "15521228172lcp@sina.com";
    private static final String SEND_PASS = "464464apple";
    private JavaMailSenderImpl mailSender;
    private Properties props;
    private Session session;

    @Autowired
    private VelocityEngine velocityEngine;

    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        Transport transport = null;
        try {
            msg.setSubject("JavaMail测试");
            // 设置邮件内容
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            msg.setText(result);
            // 设置发件人
            msg.setFrom(new InternetAddress(String.format("<%s>",SEND_NAME)));
            transport = session.getTransport();
            // 连接邮件服务器
            transport.connect(SEND_NAME, SEND_PASS);
            // 发送邮件
            transport.sendMessage(msg, new Address[] {new InternetAddress(to)});
            return true;
        } catch (MessagingException e) {
            logger.error("发送邮件失败：" + e.getMessage());
        }finally {
            // 关闭连接
            if(transport!=null){
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error("关闭连接失败:" + e.getMessage());
                }
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化配置信息
        props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.sina.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
        // 设置环境信息
        session = Session.getInstance(props);
    }

    public static void main(String [] args){
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.sina.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        // 设置环境信息
        Session session = Session.getInstance(props);

        // 创建邮件对象
        Message msg = new MimeMessage(session);
        try {
            msg.setSubject("JavaMail测试");
            // 设置邮件内容
            msg.setText("这是一封由JavaMail发送的邮件！");
            // 设置发件人
            msg.setFrom(new InternetAddress("15521228172lcp@sina.com"));

            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect("15521228172lcp", "464464apple");
            // 发送邮件
            transport.sendMessage(msg, new Address[] {new InternetAddress("1140823948@qq.com")});
            // 关闭连接
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
