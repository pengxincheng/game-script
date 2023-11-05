package com.pxc.game.ocr;


import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.pxc.game.model.Location;
import com.pxc.game.script.FuBenScript;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ResourceBundle;

/**
 * @author pengxincheng
 * @Date 2022/11/12 16:39
 * @Description
 */
public class TencentOcrUtil {

    private static final Logger logger = LoggerFactory.getLogger(TencentOcrUtil.class);

    private static  String SECRET_ID;
    private static  String SECRET_KEY;
    private static  String REGION = "ap-beijing";


    static {
        ResourceBundle ocrRes = ResourceBundle.getBundle("ocr");
        SECRET_ID = ocrRes.getString("tencent.ocr.secret_id");
        SECRET_KEY = ocrRes.getString("tencent.ocr.secret_key");
    }

    public static GeneralBasicOCRResponse getOcrResult(String imgPath) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        OcrClient client = new OcrClient(cred, REGION, clientProfile);

        File imgFile = new File(imgPath);
        String imgStr = Base64.encode(imgFile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        GeneralBasicOCRRequest req = new GeneralBasicOCRRequest();
        req.setImageBase64(imgStr);
        req.setIsPdf(false);
        req.setIsWords(false);

        // 返回的resp是一个GeneralBasicOCRResponse的实例，与请求对象对应
        GeneralBasicOCRResponse resp = client.GeneralBasicOCR(req);

        logger.info("腾讯ocr结果：{}", JSON.toJSONString(resp));
        return resp;
    }

    public static TextDetection[] generalAccurateOCR(String imgPath) throws TencentCloudSDKException {
        // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
        // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
        Credential cred = new Credential(SECRET_ID, SECRET_KEY);
        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("ocr.tencentcloudapi.com");
        // 实例化一个client选项，可选的，没有特殊需求可以跳过
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        OcrClient client = new OcrClient(cred, REGION, clientProfile);

        File imgFile = new File(imgPath);
        String imgStr = Base64.encode(imgFile);
        // 实例化一个请求对象,每个接口都会对应一个request对象
        GeneralAccurateOCRRequest req = new GeneralAccurateOCRRequest();
        req.setImageBase64(imgStr);
        req.setIsPdf(false);
        req.setIsWords(false);

        // 返回的resp是一个GeneralBasicOCRResponse的实例，与请求对象对应
        GeneralAccurateOCRResponse resp = client.GeneralAccurateOCR(req);

        logger.info("腾讯ocr结果：{}", JSON.toJSONString(resp));
        return resp.getTextDetections();
    }

    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param imgPath
     * @param word
     * @return
     */
    public static Location findCoordinate(String imgPath, String word) throws TencentCloudSDKException {

        TextDetection[] textDetections =  TencentOcrUtil.generalAccurateOCR(imgPath);

        return findCoordinate(textDetections,word);
    }

    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param textDetections
     * @param word
     * @return
     */
    public static Location findCoordinate(TextDetection[] textDetections, String word) throws TencentCloudSDKException {

        TextDetection maxLeftTextDetection = new TextDetection();
        Long maxLeft = 0L;

        for (TextDetection textDetection : textDetections) {
            if (textDetection.getDetectedText().contains(word)) {
                Long tmpLong = textDetection.getItemPolygon().getX();
                if (tmpLong > maxLeft) {
                    maxLeft = tmpLong;
                    maxLeftTextDetection = textDetection;
                }
            }

        }

        Long x = 0L;
        Long y = 0L;
        for (Coord coord : maxLeftTextDetection.getPolygon()) {
            x += coord.getX();
            y += coord.getY();
        }

        Location result = new Location();
        result.setX(x.intValue() / 4);
        result.setY(y.intValue() / 4);
        return result;
    }


    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param textDetections
     * @param word
     * @return
     */
    public static Location findLeftCoordinate(TextDetection[] textDetections, String word) throws TencentCloudSDKException {

        TextDetection minLeftTextDetection = new TextDetection();
        Long minLeft = Long.MAX_VALUE;

        for (TextDetection textDetection : textDetections) {
            if (textDetection.getDetectedText().contains(word)) {
                Long tmpLong = textDetection.getItemPolygon().getX();
                if (tmpLong < minLeft) {
                    minLeft = tmpLong;
                    minLeftTextDetection = textDetection;
                }
            }

        }

        Long x = 0L;
        Long y = 0L;
        for (Coord coord : minLeftTextDetection.getPolygon()) {
            x += coord.getX();
            y += coord.getY();
        }

        Location result = new Location();
        result.setX(x.intValue() / 4);
        result.setY(y.intValue() / 4);
        return result;
    }
}
