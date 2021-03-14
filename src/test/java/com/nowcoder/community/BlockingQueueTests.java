package com.nowcoder.community;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Description 阻塞队列
 * @Author Mr.Dong <dongcf1997@163.com>
 * @Version V1.0.0
 * @Since 1.0
 * @Date 2020/12/9
 */
public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Comsumer(blockingQueue)).start();
        new Thread(new Comsumer(blockingQueue)).start();
        new Thread(new Comsumer(blockingQueue)).start();

    }

}

class Producer implements Runnable {

    private BlockingQueue<Integer> blockingQueue = null;

    public Producer(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                blockingQueue.put(i);
                System.out.println(Thread.currentThread().getName()+"生产:"+blockingQueue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Comsumer implements Runnable {
    private BlockingQueue<Integer> blockingQueue = null;

    public Comsumer(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(new Random().nextInt(1000));
                Integer take = blockingQueue.take();
                System.out.println(Thread.currentThread().getName()+"消费:"+blockingQueue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}