package com.wjc.captcha.utill;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.wjc.captcha.dto.CaptchaResult;
import com.wjc.captcha.dto.CaptchaVo;
import com.wjc.common.login.RedisKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 王建成
 * @date 2022/5/21--16:43
 */
@Slf4j
@Component
public class CaptchaUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final int X_OFFSET = 8;

    private static final int SPEED = 500;

    @Autowired
    private HttpServletRequest request;



    /**
     * 获取验证码
     *
     * @return
     */
    public CaptchaVo captcha(PuzzleCaptcha puzzleCaptcha) {
        //这里获取的是客户端请求里的sessionid
        String id = request.getRequestedSessionId();
        if (StrUtil.isEmpty(id)){
            //这里要判断一下是否是第一次登录
            //是的话我们得需要获取这次session会话的sessionId
            id = request.getSession().getId();
        }else {
            // 删除上次验证结果
            redisTemplate.opsForHash().delete(RedisKey.CAPTCHA_RESULT, id);
        }

        Map<String, Object> cacheMap = new HashMap<>(16);
        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setImage1(ImageConvertUtil.toDataUri(puzzleCaptcha.getArtwork(), "png"));
        captchaVo.setImage2(ImageConvertUtil.toDataUri(puzzleCaptcha.getVacancy(), "png"));

        // 偏移量
        cacheMap.put("x", puzzleCaptcha.getX());
        cacheMap.put("time", System.currentTimeMillis());
        cacheMap.put("width", puzzleCaptcha.getWidth());
        redisTemplate.opsForHash().put(RedisKey.CAPTCHA, id, cacheMap);
        return captchaVo;
    }

    /**
     * 验证码验证
     *
     * @param map
     * @return
     */
    public CaptchaResult verify(Map<String, Object> map) {
        String id = request.getRequestedSessionId();
        CaptchaResult result = new CaptchaResult();
        result.setSuccess(false);


        // 偏移量
        Integer vx = MsiStrutill.toInt(map.get("x"));
        // 宽度
        Integer width = MsiStrutill.toInt(map.get("width"), 1);

        //缓存
        Map<String, Object> cacheMap = (Map<String, Object>) redisTemplate.opsForHash().get(RedisKey.CAPTCHA,id);
        if (cacheMap == null) {
            result.setMessage("请重新获取图片！");
            return result;
        }
        Integer x = MsiStrutill.toInt(cacheMap.get("x"));
        Integer realWidth = MsiStrutill.toInt(cacheMap.get("width"));
        Long time = MsiStrutill.toLong(cacheMap.get("time"));
        long s = System.currentTimeMillis() - time;

        // 查看前端的缩放比例
        double ratio = NumberUtil.div(realWidth, width).doubleValue();

        if (x == null || vx == null) {
            redisTemplate.opsForHash().delete(RedisKey.CAPTCHA, id);
            return result;
        } else if (Math.abs(x - (vx * ratio)) > X_OFFSET * ratio || s < SPEED) {
            redisTemplate.opsForHash().delete(RedisKey.CAPTCHA, id);
            return result;
        }
        result.setSuccess(true);
        redisTemplate.opsForHash().delete(RedisKey.CAPTCHA, id);
        redisTemplate.opsForHash().put(RedisKey.CAPTCHA_RESULT, id, result);

        return result;
    }
}