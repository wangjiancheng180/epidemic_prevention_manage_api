package com.wjc.service.university.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wjc.common.external.ChinaEpidemicData;
import com.wjc.common.external.Wresult;
import com.wjc.service.university.StatisticsService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * @author 王建成
 * @date 2022/5/6--23:28
 */

@Service
public class StatisticsServiceImpl implements StatisticsService {


    @Override
    public List<ChinaEpidemicData> queryChinaEpidemicData() {
        long timeMillis = System.currentTimeMillis();
        String key = "appid16971formatjsontime"+timeMillis;
        String seceretKey = "03bf39f3be20200b83695849016db9a0";
        String dig= DigestUtil.md5Hex(key+seceretKey);
        String url ="http://grnx.api.storeapi.net/api/94/219?appid=16971&format=json&time="+timeMillis+"&sign="+dig;
        String result = HttpRequest.get(url)
                .timeout(20000)
                .execute().body();
        Wresult wresult = JSONUtil.toBean(result, Wresult.class);

        return wresult.getRetdata();
    }
}
