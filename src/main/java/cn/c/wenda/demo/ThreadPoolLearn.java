/**
 * 
 */
package cn.c.wenda.demo;

import java.util.concurrent.*;

/**
 * @author lcplcp
 */
/**
 * 池化技术学习
 * @author lcplcp
 */
public class ThreadPoolLearn {
	public static void main(String [] args){

		ExecutorService threadPool = Executors.newCachedThreadPool();
		/*
		BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(100);
		Runnable producer = new Producer(blockingQueue);
		threadPool.execute(producer);
		threadPool.execute(new Comsumer(blockingQueue));
		*/
		//callable test

		Future<String> future = threadPool.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				System.out.println("call do");
				Thread.sleep(3000);
				return "Success";
			}
		});
		String fstr = null;
		try {
			System.out.println("before");
			fstr = future.get();
			System.out.println("after");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println(fstr);

	}
}

/**
 * 生产者
 */
class Producer implements Runnable{
	BlockingQueue<String> blockingQueue;
	public Producer(BlockingQueue<String> blockingQueue){
		this.blockingQueue = blockingQueue;
	}
	@Override
	public void run() {
		for(int i=0;i<100;i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			blockingQueue.add("第"+i + "个元素");
		}
	}
}

/**
 * 消费者
 *
 */
class Comsumer implements  Runnable{
	BlockingQueue<String> blockingQueue;
	public Comsumer(BlockingQueue<String> blockingQueue){
		this.blockingQueue = blockingQueue;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String t = null;
		while(true){
			//取blockqueue中的值
			try {
				t= blockingQueue.take(	);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
			System.out.println(t);
		}
	}
}
