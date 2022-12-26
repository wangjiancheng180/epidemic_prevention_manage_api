package com.wjc.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 王建成
 * @date 2022/12/18--14:21
 */
@Slf4j
@Configuration
public class MessageListenerConfig {

    @Resource
    private InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String database;


    //业务处理异步线程池，线程池参数可以根据您的业务特点调整，或者您也可以用其他异步方式处理接收到的消息。
    private  ExecutorService executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue(50000));

    @Bean(name="messageListener")
    public MessageListener messageListener(){
        return new MessageListener() {
            @Override
            public void onMessage(final Message message) {
                try {
                    //1.收到消息之后一定要ACK。
                    // 推荐做法：创建Session选择Session.AUTO_ACKNOWLEDGE，这里会自动ACK。
                    // 其他做法：创建Session选择Session.CLIENT_ACKNOWLEDGE，这里一定要调message.acknowledge()来ACK。
                    // message.acknowledge();
                    //2.建议异步处理收到的消息，确保onMessage函数里没有耗时逻辑。
                    // 如果业务处理耗时过程过长阻塞住线程，可能会影响SDK收到消息后的正常回调。
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            processMessage(message);
                        }
                    });
                } catch (Exception e) {
                    log.error("submit task occurs exception ", e);
                }
            }
        };
    }

//    private javax.jms.MessageListenerConfig messageListener =
//    };

    /**
     * 在这里处理您收到消息后的具体业务逻辑。
     */
    private  void processMessage(Message message) {
        try {
            byte[] body = message.getBody(byte[].class);
            String content = new String(body);
            String topic = message.getStringProperty("topic");
            String messageId = message.getStringProperty("messageId");

            Map contentMap = JSONUtil.toBean(content, Map.class);
           //存储数据
            saveDB(contentMap);
            //将设备数据时时发送给前端通过前端指定要看的设备信息，主要是通过前端发送设备名称切换发送
            String deviceName = (String) contentMap.get("deviceName");
            if (CollUtil.isNotEmpty(WebSocketServer.webSocketSet)){
                for (WebSocketServer client:WebSocketServer.webSocketSet
                     ) {
                    //如果连接的客户端是没有指定要发送的设备那么就全部信息都推送
                    if(StrUtil.isEmpty(client.getDeviceName())){
                        //默认给该客户端随机添加一个目标设备
                        client.setDeviceName(deviceName);
                        WebSocketServer.sendInfo(content,client.getSid());
                    }else {
                        //如果当前客户端指定的设备是当前接收到的设备就指定推送
                        if(client.getDeviceName().equals(deviceName)){
                            WebSocketServer.sendInfo(content,client.getSid());
                        }
                    }
                }

//                WebSocketServer.sendInfo(content,null);
            }
            log.info("接收到的信息：message"
                    + ",\n topic = " + topic
                    + ",\n messageId = " + messageId
                    + ",\n content = " + content);
        } catch (Exception e) {
            log.error("processMessage occurs error ", e);
        }
    }

    /**
     * 添加设备上报数据到时序数据库influxDB
     * @param content
     */
    private void saveDB(Map content){
//        String sql =
        String deviceName = (String) content.get("deviceName");
        String productKey = (String) content.get("productKey");
        Map<String,Map> items = (Map) content.get("items");

        Integer flameout = null,fuel = null,temperature = null,humidity = null,speed = null,mileage = null;
        BigDecimal longitude = null,latitude=null;
        for (Map.Entry<String,Map> entry:items.entrySet()
             ) {
            switch (entry.getKey()){
                case "flameout":
                        flameout= (Integer) entry.getValue().get("value");
                        break;
                case "fuel":
                        fuel = (Integer) entry.getValue().get("value");
                        break;
                case "temperature":
                        temperature = (Integer) entry.getValue().get("value");
                        break;
                case "humidity":
                        humidity= (Integer) entry.getValue().get("value");
                        break;
                case "speed":
                        speed = (Integer) entry.getValue().get("value");
                        break;
                case  "mileage":
                        mileage = (Integer) entry.getValue().get("value");
                        break;
                case  "longitude":
                        longitude = (BigDecimal) entry.getValue().get("value");
                        break;
                case  "latitude":
                        latitude = (BigDecimal) entry.getValue().get("value");
                        break;
            }
        }
        Point point = Point.measurement("device")
                .tag("device_name",deviceName)
                .tag("product_key",productKey)
                .addField("flameout",flameout)
                .addField("fuel",fuel)
                .addField("temperature",temperature)
                .addField("humidity",humidity)
                .addField("speed",speed)
                .addField("mileage",mileage)
                .addField("longitude",longitude)
                .addField("latitude",latitude).build();

        influxDB.write(point);
//        log.info(StrUtil.format("设备上报数据sql:{}"),sql);
//        influxDB.write(sql);
    }
}
