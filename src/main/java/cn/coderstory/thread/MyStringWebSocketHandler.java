package cn.coderstory.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class MyStringWebSocketHandler extends TextWebSocketHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    public Map<String, TestThread> sessionAndThread = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("和客户端建立连接");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.close(CloseStatus.SERVER_ERROR);
        log.error("连接异常", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.info("和客户端断开连接");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {


        // 获取到客户端发送过来的消息
        String receiveMessage = message.getPayload();
        log.info(receiveMessage);
        if (receiveMessage.startsWith("getSession")) {
            session.sendMessage(new TextMessage(session.getId()));
        }
        if (receiveMessage.startsWith("run")) {
            // 启动任务
            TestThread testThread = new TestThread();
            sessionAndThread.put(session.getId(), testThread);
            // 执行第一阶段
            testThread.start();
            session.sendMessage(new TextMessage("线程启动成功"));
        }
        if (receiveMessage.startsWith("next")) {
            TestThread thread = sessionAndThread.get(session.getId());
            thread.notify();

            session.sendMessage(new TextMessage(thread.threadContent.get("state")));
        }

        // 发送消息给客户端
        // session.sendMessage(new TextMessage(fakeAi(receiveMessage)));
        // 关闭连接
        // session.close(CloseStatus.NORMAL);
    }

    private static String fakeAi(String input) {
        return "你说什么？没听";
    }
}
