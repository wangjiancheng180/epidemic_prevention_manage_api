package com.wjc.websocket;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
@Data
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
   private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    public static Set<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    public Session session;

    //接收sid
    private String sid="";

    //这里需要一个推送标识用来判定推送哪个设备信息
    private String deviceName;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("session:{}",session.toString());
        this.sid=sid;
    }

   /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为:{}",getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        //这里收到更改
        log.info(StrUtil.format("收到客户端信息:{}",message));
        this.deviceName = message;
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误,",error);
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private void sendByte(byte[] mjpeg) throws IOException {
        this.session.getBasicRemote().sendBinary(ByteBuffer.wrap(mjpeg));
    }


    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid){
        log.info("推送内容:{}",message.length());
        for (WebSocketServer item : webSocketSet) {
            try {

                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static void sendImg(byte[] mjpeg, String sid) {
//        log.info("发送webserver:{}",webSocketSet.size());
        for (WebSocketServer item : webSocketSet) {
            try{
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
//                    log.info("发送");
                    item.sendByte(mjpeg);
                }else if(item.sid.equals(sid)){
                    item.sendByte(mjpeg);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}

