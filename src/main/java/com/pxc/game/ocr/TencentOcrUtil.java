package com.pxc.game.ocr;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.baidu.aip.ocr.AipOcr;
import com.pxc.game.model.Location;
import com.sun.prism.Image;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.*;

import java.io.File;
import java.util.ResourceBundle;

/**
 * @author pengxincheng
 * @Date 2022/11/12 16:39
 * @Description
 */
public class TencentOcrUtil {
    private static  String SECRET_ID;
    private static  String SECRET_KEY;
    private static  String REGION = "ap-beijing";


    static {
        ResourceBundle ocrRes = ResourceBundle.getBundle("ocr");
        SECRET_ID = ocrRes.getString("tencent.ocr.secret_id");
        SECRET_KEY = ocrRes.getString("tencent.ocr.secret_key");
    }

    /**
     * 在图片上找文字 片并返回坐标
     *
     * @param imgPath
     * @param word
     * @return
     */
    public static Location findCoordinate(String imgPath, String word) throws TencentCloudSDKException {

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

        TextDetection maxLeftTextDetection = new TextDetection();
        Long maxLeft = 0L;

        // 返回的resp是一个GeneralBasicOCRResponse的实例，与请求对象对应
        GeneralBasicOCRResponse resp = client.GeneralBasicOCR(req);
        for (TextDetection textDetection : resp.getTextDetections()) {
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
}
