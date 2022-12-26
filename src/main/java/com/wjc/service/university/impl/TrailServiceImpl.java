package com.wjc.service.university.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wjc.param.visualization.Spot;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wjc.service.university.TrailService;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 王建成
* @date 2022/5/4--20:35
*/     
@Service
public class TrailServiceImpl implements TrailService{


    @Resource
    private InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String database;

    @Override
    public List<Object>queryPosition(Long studentId){
        String sql = "select * from trail where student_id='"+studentId+"'order by time desc limit 1";
        return fetchRecords(sql);
    }

    @Override
    public List<Object> queryLine(Long studentId) {
        String sql = "select * from trail where student_id='"+studentId+"'and time > NOW()-48h";
        return fetchRecords(sql);
    }

    @Override
    public boolean simulation(Spot spot) {
        Point point = Point.measurement("trail")
                .tag("student_id", spot.getStudentId())
                .addField("lng", spot.getLng())
                .addField("lat", spot.getLat())
                .build();
        insert(point);
        return true;
    }

    @Override
    public List<Spot> queryAllPoints() {
        String sql = "select * from trail where time > NOW()-48h";

        return fetchResults(sql,Spot.class);
    }

    public List<Object> fetchRecords(String query){
        List<Object> results = new ArrayList<Object>();
        QueryResult queryResult = influxDB.query(new Query(query));
           queryResult.getResults().forEach(result->{
               if(CollUtil.isNotEmpty(result.getSeries())){
                   result.getSeries().forEach(serial->{
                       List<String> columns = serial.getColumns();
                       int fieldSize = columns.size();
                       serial.getValues().forEach(value->{
                           Map<String,Object> obj = new HashMap<String,Object>();
                           for(int i=0;i<fieldSize;i++){
                               obj.put(columns.get(i), value.get(i));
                           }
                           results.add(obj);
                       });
                   });
               }

           });

        return results;
    }

    public void insert(Point point){
        influxDB.write(point);
    }


    public Spot getCenter(){
        String sql = "select * from trail where time > NOW()-48h";
        List<Spot> spots = fetchResults(sql,Spot.class);
        return getCenterPointFromListOfCoordinates(spots);
    }

    /**
     * 查询，返回对象的list集合
     * @param query
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> List<T> fetchResults(String query, Class<?> clasz){
        List results = new ArrayList<>();
        QueryResult queryResult = influxDB.query(new Query(query, database));
        queryResult.getResults().forEach(result->{
            result.getSeries().forEach(serial->{
                List<String> columns = serial.getColumns();
                int fieldSize = columns.size();
                serial.getValues().forEach(value->{
                    Object obj = null;
                    try {
                        obj = clasz.newInstance();
                        for(int i=0;i<fieldSize;i++){
                            String fieldName = columns.get(i);
                            //解决驼峰映射
                            if(fieldName.contains("_")){
                                int index = fieldName.indexOf("_");
                                String s1 = fieldName.substring(0, index);
                                String s2 = fieldName.substring(index + 1);
                                String s3 = s2.substring(0, 1).toUpperCase().concat(s2.substring((1)));
                                fieldName = s1.concat(s3);
                            }
                            Field field = clasz.getDeclaredField(fieldName);
                            field.setAccessible(true);
                            Class<?> type = field.getType();
                            if(type == double.class){
                                field.set(obj,Double.valueOf(value.get(i).toString()));
                            }else{
                                field.set(obj, value.get(i));
                            }
                        }
                    } catch (NoSuchFieldException | SecurityException | InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    results.add(obj);
                });
            });
        });
        return results;
    }

    /**
     * 多点计算中间点
     * @param spots
     * @return
     */
    public Spot getCenterPointFromListOfCoordinates(List<Spot> spots){
            int total = spots.size();
            double lng = 0, lat = 0;
        for (Spot spot : spots) {
            lng += spot.getLng()*Math.PI/180;
            lat += spot.getLat()*Math.PI/180;
        }
        lat /= total;
        lng /= total;
        Spot spot = new Spot();
        spot.setLng(lng * 180 / Math.PI);
        spot.setLat(lat * 180 / Math.PI);
        return spot;
    }

}
