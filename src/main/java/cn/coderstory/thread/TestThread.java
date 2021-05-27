package cn.coderstory.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class TestThread extends Thread {
    Map<String, String> threadContent = new HashMap<>();
    int state = 1;
    // TODO 如果websock被关闭 则需要关闭掉
    boolean needBreak = false;

    public TestThread() {

    }

    @Override
    public void run() {
        final CountDownLatch latch = new CountDownLatch(1);
        System.out.println("阶段" + state);
        threadContent.put("state", state + "");
        try {
            state++;
            // TODO 需要有个存latch和返回值的地方 返回值要发送给前端 latch需要点击下一步的时候调用一次countDown 然后就会唤醒本线程
            // 应该需要一个消息队列 存入这些东西 然后有个消费者 返回数据给前端  点击下一步的时候 还需要拿出里面的 latch
            latch.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
