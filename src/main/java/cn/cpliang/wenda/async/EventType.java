package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/14.
 */

/**
 * 枚举类型，用于存储事件类型
 */
public enum EventType {
    LIKE(0),
    LOGIN(1),
    COMMENT(2),
    MAIL(3),
    REGISTER_ACTIVE(4);

    private int value;
    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return this.value;
    }
}
