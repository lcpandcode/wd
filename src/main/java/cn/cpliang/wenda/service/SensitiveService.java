package cn.cpliang.wenda.service;

import org.apache.commons.lang.CharRange;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcplcp on 2017/5/8.
 */
@Service
public class SensitiveService implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private static String SENSITIVE_REPLACE = "敏感词";
    @Override
    public void afterPropertiesSet() throws Exception {
        root = new TrieNode();
        InputStream inputStream = null;
        InputStreamReader r = null;
        BufferedReader reader = null;
        try {
            inputStream = this.getClass().getResourceAsStream("/sensitiveWord.txt");
            r = new InputStreamReader(inputStream);
            reader = new BufferedReader(r);
            String lineTest = null;
            while ((lineTest=reader.readLine())!=null){
                addSensitiveWord(lineTest);
            }
        }catch (Exception e){
            logger.error("读取敏感词文件失败："+e.getMessage());
        }finally {
            if(inputStream!=null){
                inputStream.close();
            }
            if(r!=null){
                r.close();
            }
            if(reader!=null){
                reader.close();
            }
        }

    }

    //节点内部类
    private  class TrieNode{
        //是否是有子节点（是否是最后一个）
        boolean isEnd = false;
        //子节点
        Map<Character,TrieNode> map = new HashMap<>();
        //新增子节点方法
        public TrieNode add(Character character,TrieNode node){
            if(map.get(character)!=null){
                return this;
            }
            map.put(character,node);
            return node;
        }
        //移除
        public TrieNode remove(Character character){
            return map.remove(character);
        }
        //获取子节点
        public TrieNode getSonNode(Character character){
            return map.get(character);
        }
        //设置是否是最后一个词语
        public void setIsEnd(boolean isEnd){
            this.isEnd = isEnd;
        }
        public boolean getIsEnd(){
            return this.isEnd;
        }

    }


    //根节点
    //TrieNode root = new TrieNode();//测试spring会不会执行该语句
    TrieNode root = null;
    //判断是否是一个合法符号
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }
    //新增敏感词，返回对应的尾部节点
    public TrieNode addSensitiveWord(String word){
        if(StringUtils.isBlank(word)){
            throw new RuntimeException("输入词汇有误，请检查是否输入了空字符串，多于的回车符号等！");
        }
        char [] characters = word.toCharArray();
        TrieNode next = root;
        TrieNode tem = null;
        for(Character c:characters){
            if(isSymbol(c)){
                continue;
            }
            if(next.getSonNode(c)==null){
                tem = new TrieNode();
                next.add(c,tem);
                next = tem;
                continue;
            }
            next = next.getSonNode(c);
        }
        //设置尾部节点
        next.setIsEnd(true);
        return next;
    }

    //过滤方法，返回过滤后的字符
    public String filter(String text){
        StringBuilder rtn = new StringBuilder();
        TrieNode tem = root;
        for(int i=0,j=0;i<text.length();){
            if(isSymbol(text.charAt(i))){
                i++;
                continue;
            }
            if(tem==null){
                rtn.append(text.charAt(i));
                i++;
                j = i;
                tem = root;
                continue;
            }else if(tem.isEnd){
                //发现敏感词
                rtn.append(SENSITIVE_REPLACE);
                i = j;
                tem = root;
            }else if(j<text.length()){
                tem = tem.getSonNode(text.charAt(j));
                j++;
            }else{
                i++;
                j = i;
                tem = root;
            }
        }
        return rtn.toString();
    }

    public static void main(String [] args){
        SensitiveService service = new SensitiveService();
        try{
            service.afterPropertiesSet();
        }catch (Exception e){
            e.printStackTrace();
        }
        String str = service.filter("hh呵呵你是傻逼拗色情口阿里反人斯反人类柯达世界分反社会的解放军");
        System.out.println(str);

    }
}
