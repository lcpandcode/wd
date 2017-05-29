package cn.cpliang.wenda;

/**
 * Created by lcplcp on 2017/5/9.
 */
public class ThreadTest {
    public static Object object = new Object();

    public static void main(String [] args){
        Thread t = new Thread(new Thread1());
        t.start();
        System.out.println("main 方法");
        System.out.println(object.toString());
    }
}

class Thread1 implements Runnable{

    @Override
    public void run() {
        synchronized (ThreadTest.object){
            try {
                System.out.println("开始睡觉");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

