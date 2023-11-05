package com.pxc.game.util;

import com.alibaba.fastjson.JSON;
import com.pxc.game.model.Location;
import com.pxc.game.ocr.OcrUtil;
import com.pxc.game.ocr.TencentOcrUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.GeneralBasicOCRResponse;
import com.tencentcloudapi.ocr.v20181119.models.TextDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author: pengxincheng
 * @Date: 2019/12/5 13:59
 * @Description:
 */
public class OperationUtils {

    private static final Logger logger = LoggerFactory.getLogger(OperationUtils.class);

    // 获取屏幕的尺寸
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * 截屏，返回图片路径
     *
     * @return
     */
    public static String screenshot() throws IOException, AWTException {
        logger.info("开始截图。屏幕宽度为:{},高度为:{} ", screenSize.getWidth(), screenSize.getHeight());
        // 截取屏幕
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(screenSize));
        // 创建一个用于保存图片的文件夹
        String path = "image/screenCapture/";
        File screenCaptureDirectory = new File(path);
        if (!screenCaptureDirectory.exists()) {
            screenCaptureDirectory.mkdirs();
        }
        String imageName = System.currentTimeMillis() + ".png";
        File imageFile = new File(path, imageName);
        // 以指定的图片格式将截取的图片写到指定的文件
        ImageIO.write(image, "png", imageFile);
        logger.info("截图保存完成，路径为：{}" , path + imageName);
        return path + imageName;
    }

    /**
     * 找文字位置
     *
     * @param filePath
     * @param keyWord
     * @return
     */
    public static Location getWordLocation(String filePath, String keyWord) throws TencentCloudSDKException {
       // return OcrUtil.findCoordinate(filePath, keyWord);
        return TencentOcrUtil.findCoordinate(filePath,keyWord);
    }
    /**
     * 找文字位置
     *
     * @param textDetections
     * @param keyWord
     * @return
     */
    public static Location getWordLocationV2( TextDetection[] textDetections, String keyWord) throws TencentCloudSDKException {
        // return OcrUtil.findCoordinate(filePath, keyWord);
        return TencentOcrUtil.findCoordinate(textDetections,keyWord);
    }

    /**
     * 找并且点击
     *
     * @param keyWord
     * @param imgPath
     * @throws IOException
     * @throws AWTException
     */
    public static void findAndClick(String keyWord,String imgPath) throws Exception {
        Location location = getWordLocation(imgPath, keyWord);
        logger.info("匹配到需要点击的位置：location={}", JSON.toJSONString(location));

        Robot robot = new Robot();
        robot.delay(1000);

        robot.mouseMove(location.getX() + 10, location.getY() + 10);
        robot.delay(1000);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        logger.info("点击成功！");
    }


    /**
     * 找并且点击 相同的点击左边的
     *
     * @param keyWord
     * @param imgPath
     * @throws IOException
     * @throws AWTException
     */
    public static void findAndClickLeft(String keyWord,String imgPath) throws Exception {
       TextDetection[] textDetections =  TencentOcrUtil.generalAccurateOCR(imgPath);

        Location location =   TencentOcrUtil.findLeftCoordinate(textDetections,keyWord);

        logger.info("匹配到需要点击的位置：location={}", JSON.toJSONString(location));

        Robot robot = new Robot();
        robot.delay(1000);

        robot.mouseMove(location.getX() + 10, location.getY() + 10);
        robot.delay(1000);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        logger.info("点击成功！");
    }

    /**
     * 点击活动
     * @param huoDongName
     * @return
     */
    public static boolean clickHuoDong(String huoDongName) throws Exception {

        // 游戏主页 活动
        String imgPath = OperationUtils.screenshot();
        Location location = getWordLocation(imgPath, "长安城");
        Robot robot = new Robot();
        robot.delay(1000);
        robot.mouseMove(location.getX(),location.getY());
        robot.delay(1000);

        // 模拟按下 Alt 键
        robot.keyPress(KeyEvent.VK_ALT);
        // 延迟一下，让 Alt 键被正确地按下并注册到系统中
        robot.delay(200);
        // 模拟按下 x 键
        robot.keyPress(KeyEvent.VK_C);
        // 释放 x 键
        robot.delay(500);
        robot.keyRelease(KeyEvent.VK_C);
        // 释放 Alt 键
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.delay(1000);


        // 活动页
        logger.info("判断活动页");

        TimeUnit.SECONDS.sleep(2);
        String richangpaht = OperationUtils.screenshot();
        OperationUtils.findAndClick("日常活动",richangpaht);
        TimeUnit.SECONDS.sleep(2);


        String imgPath1 = OperationUtils.screenshot();
        TextDetection[] textDetections = TencentOcrUtil.generalAccurateOCR(imgPath1);
        Location location1 = getWordLocationV2(textDetections, huoDongName);
        logger.info("location1={}",JSON.toJSONString(location1));

        for (TextDetection textDetection : textDetections) {
               if(Objects.equals(textDetection.getDetectedText(),"参加")){
                   Long tmpX = textDetection.getItemPolygon().getX();
                   Long tmpY = textDetection.getItemPolygon().getY();
                   if(tmpX.intValue() >= location1.getX() && tmpY.intValue() >= location1.getY()){

                       robot.mouseMove(tmpX.intValue(),tmpY.intValue());
                       robot.delay(1000);
                       robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                       robot.delay(300);

                       robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                       break;
                   }
               }
        }
        return true;
    }
}
