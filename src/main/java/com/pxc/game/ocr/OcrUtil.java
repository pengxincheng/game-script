package com.pxc.game.ocr;

import com.baidu.aip.ocr.AipOcr;
import com.pxc.game.model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: pengxincheng
 * @Date: 2019/12/5 12:09
 * @Description:
 */
public class OcrUtil {

    //设置APPID/AK/SK
    public static final String APP_ID = "11787520";
    public static final String API_KEY = "gj4GtsYBfOSvYtLCCGPpIc5y";
    public static final String SECRET_KEY = "Z2q44GaY1u4ZPj06vMnpSOMjyfosMwpp";


    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param imgPath
     * @param word
     * @return
     */
    public static Location findCoordinate(String imgPath, String word) {
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
        JSONObject res = client.general(imgPath, new HashMap<String, String>());

        JSONArray words_result = res.getJSONArray("words_result");
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
