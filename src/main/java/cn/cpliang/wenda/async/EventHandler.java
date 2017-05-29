package cn.cpliang.wenda.async;

/**
 * Created by lcplcp on 2017/5/14.
 */


import java.util.List;

/**
 * 事件处理器：用于处理事件队列里面的事件，被eventConsumer调用
 * doHandler：model是具体的事件模型，它需要由调用者（一般是comsumer）传进来
 *
 */
public interface EventHandler {
    public void doHandler(EventModel model);

    /**
     *
     * @return 表明该接口是什么类型的handler，list表明handler可以支持多个业务
     * 每个实现了EventHandler的类都需要利用该方法声明自己是什么类型的hadler，因为在comsumer中是通过一个
     * map结构存储所有的hadler实例,key值对应的是该函数的返回值，并且comsumer通过该方法返回的值进行具体接口的调用
     *
     */
    public List<EventType> getHandlerType();

}
