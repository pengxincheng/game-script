package com.pxc.game.script;

import com.pxc.game.App;
import com.pxc.game.ocr.SelfOcrUtil;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
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
     * @throws Exception
     */
    public static void zhuaGui() throws Exception{
        Robot robot = new Robot();
        SecureRandom random = new SecureRandom();
        try {
            logger.info("开始检测是否抓完一轮鬼");
            boolean b = SelfOcrUtil.existKeyWord("是否继续抓鬼");

            // boolean b = true;
            logger.info("查找结果：{}",b);
            if (b) {
                OperationUtils.findAndClick("确定");
                TimeUnit.SECONDS.sleep(3);
                OperationUtils.findAndClick("抓鬼任务");
                TimeUnit.SECONDS.sleep(3);
                OperationUtils.findAndClick("捉鬼");
                TimeUnit.SECONDS.sleep(3);


                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.delay(100);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        robot.mouseMove(random.nextInt(200), random.nextInt(500));
    }


    /**
     * 门派
     * @throws Exception
     */
    public static void menPai() throws Exception{
        Robot robot = new Robot();
        SecureRandom random = new SecureRandom();
        while (true) {
            try {
                logger.info("开始检测门派任务");
                boolean b = SelfOcrUtil.existKeyWord("挑战");

                // boolean b = true;
                logger.info("查找结果：{}", b);
                if (b) {
                    OperationUtils.findAndClick("挑战");

                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(100);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                    TimeUnit.SECONDS.sleep(5);
                    OperationUtils.findAndClick("进入战斗");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(30);

        }
    }

}
