package cn.cpliang.wenda.model;

/**
 * Created by lcplcp on 2017/5/10.
 */

/**
 * 该类是一个存储评论类型的封装类
 * 所有的回复，问题的评论等都可以归为评论
 */
public class EntityType {

    public final  static int ENTITY_QUESTION = 1;//1代表问题的评论
    public final static int ENTITY_COMMENT = 2;//2代表评论的回复
    public final static int ENTITY_USER = 3;//3代表用户实体
}
