package com.pxc.game.ocr;

import cn.hutool.core.collection.CollectionUtil;
import com.baidu.aip.ocr.AipOcr;
import com.pxc.game.model.Location;
import com.pxc.game.util.OperationUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: pengxincheng
 * @Date: 2019/12/5 12:09
 * @Description: 百度ocr识别工具
 */
public class OcrUtil {

    private static final Logger logger = LoggerFactory.getLogger(OperationUtils.class);

    public static final String APP_ID = "";
    public static final String API_KEY = "";
    public static final String SECRET_KEY = "";
    public static AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);


    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param imgPath
     * @param word
     * @return
     */
    public static Location findCoordinate(String imgPath, String word) {

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        HashMap<String, String> paramMap = new HashMap<>(16);
        paramMap.put("vertexes_location", "true");

        JSONObject res = client.accurateGeneral(imgPath, paramMap);
        logger.info("res={}", res);

        JSONArray words_result = res.getJSONArray("words_result");

        logger.info("ocr返回数据words_result={}", words_result);

        Location result = new Location();


        JSONObject maxLeftLocation = new JSONObject();
        Integer maxLeft = 0;
        for (Object w : words_result) {
            JSONObject wObj =  (JSONObject) w;
            String words = wObj.getString("words");
            if (words.contains(word)) {
                Integer tmpInt = wObj.getJSONObject("location").getInt("left");
                if(tmpInt > maxLeft){
                    maxLeft = tmpInt;
                    maxLeftLocation = wObj;
                }
            }
        }


        int x = 0;
        int y = 0;
        // 文字外接多边形的丁点坐标
        JSONArray locations = maxLeftLocation.getJSONArray("vertexes_location");
        if (CollectionUtil.isNotEmpty(locations)) {
            for (Object location : locations) {
                JSONObject jsonLocation = ((JSONObject) location);
                x += jsonLocation.getInt("x");
                y += jsonLocation.getInt("y");
            }
            result.setX(x / 4);
            result.setY(y / 4);
        }
        return result;
    }


    /**
     * 获取图片识别结果都有哪些文字
     * 普通文字识别，主要判断是否有想要点击的接口
     * @param imgPath
     * @return
     */
    public static String getOcrResult(String imgPath) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");

        // 参数为本地图片路径
        String image = imgPath;
        JSONObject res = client.basicGeneral(image, options);
        logger.info("ocr返回数据res={}", res.toString());
        return res.toString();
    }


    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param imgPath
     * @param word
     * @return
     */
    public static Location findCoordinatePrecision(String imgPath, String word) {
        Map<Integer, Integer> map = new HashMap<>();
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量

        // 调用接口

        HashMap<String, String> paramMap = new HashMap<>();
        //aramMap.put("vertexes_location", "true");

        JSONObject res = client.accurateGeneral(imgPath, paramMap);
        logger.info("res={}", res);

        JSONArray words_result = res.getJSONArray("words_result");

        logger.info("ocr返回数据words_result={}", words_result);

        Location result = new Location();
        for (Object w : words_result) {
            String words = ((JSONObject) w).getString("words");
            if (words.contains(word)) {
                JSONObject location = ((JSONObject) w).getJSONObject("location");
                result.setX(location.getInt("left"));
                result.setY(location.getInt("top"));
                break;
            }
        }
        return result;
    }

}
