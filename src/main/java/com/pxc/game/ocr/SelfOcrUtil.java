package com.pxc.game.ocr;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pxc.game.util.OperationUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pengxincheng
 * @date 2022/3/17 6:45 下午
 */
public class SelfOcrUtil {

    private static final String selfOcrUrl = "http://ocr.xinchengkeji.work/api/tr-run/";

    public static Boolean existKeyWord(String keyWork) throws IOException, AWTException {
        Map<String, Object> paramMap = new HashMap<>();

        String filePath = OperationUtils.screenshot();
        paramMap.put("file", new File(filePath));

        String result2 = HttpRequest.post(selfOcrUrl)
                .header(Header.USER_AGENT, "Hutool http")//头信息，多个头信息多次调用此方法即可
                .form(paramMap)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();
        JSONObject jsonResult = JSON.parseObject(result2);
        String ocrResult = jsonResult.getJSONObject("data").getString("raw_out");

        return ocrResult.contains(keyWork);
    }
}
