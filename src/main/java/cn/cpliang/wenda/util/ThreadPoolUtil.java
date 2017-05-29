package cn.cpliang.wenda.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lcplcp on 2017/5/15.
 */
@Component
public class ThreadPoolUtil {
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public void execute(Runnable runnable){
        threadPool.execute(runnable);
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }
}
