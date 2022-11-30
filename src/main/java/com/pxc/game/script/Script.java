package com.pxc.game.script;

import cn.hutool.core.io.FileUtil;
import com.pxc.game.App;
import com.pxc.game.ocr.OcrUtil;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author: pengxincheng
 * @Date: 2019/12/5 13:51
 * @Description:
 */
public class Script {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * 抓鬼
     *
     * @throws Exception
     */
    public static void zhuaGui() throws Exception {
        Robot robot = new Robot();
        SecureRandom random = new SecureRandom();
        while (true) {
            try {
                logger.info("开始检测是否抓完一轮鬼");
                String imagePath = OperationUtils.screenshot();
                String words = OcrUtil.getOcrResult(imagePath);
                //  boolean b = SelfOcrUtil.existKeyWord("是否继续抓鬼");
                boolean b = words.contains("是否继续捉鬼");
              //  boolean b = true;
                logger.info("查找结果：{}", b);
                if (b) {
                    OperationUtils.findAndClick("确定", imagePath);

                    TimeUnit.SECONDS.sleep(7);
                    String taskImagePath = OperationUtils.screenshot();
                    OperationUtils.findAndClick("捉鬼任务", taskImagePath);
                    TimeUnit.SECONDS.sleep(3);

                    String zhuaguiImagePath = OperationUtils.screenshot();
                    OperationUtils.findAndClick("1/10", zhuaguiImagePath);
                    TimeUnit.SECONDS.sleep(3);

                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(100);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                FileUtil.del(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.MINUTES.sleep(1);
            robot.mouseMove(random.nextInt(200), random.nextInt(500));
        }
    }


    /**
     * 门派
     *
     * @throws Exception
     */
    public static void menPai() throws Exception {
        Robot robot = new Robot();
        while (true) {
            try {
                logger.info("开始检测门派任务");
                // boolean b = SelfOcrUtil.existKeyWord("挑战");
                String imagePath = OperationUtils.screenshot();
                String words = OcrUtil.getOcrResult(imagePath);
                boolean b = words.contains("挑战") && words.contains("门派");
                logger.info("查找结果：{}", b);
                if (b) {
                    OperationUtils.findAndClick("挑战", imagePath);

                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(100);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                    TimeUnit.SECONDS.sleep(5);
                    OperationUtils.findAndClick("进入战斗", imagePath);
                }
                FileUtil.del(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(30);

        }
    }

}
