package com.pxc.game.script;

import cn.hutool.core.io.FileUtil;
import com.pxc.game.ocr.OcrUtil;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author pengxincheng
 * @Date 2022/12/5 20:59
 * @Description
 */
public class MiJingScript {
    private static final Logger logger = LoggerFactory.getLogger(MiJingScript.class);
    /**
     * 抓鬼
     *
     * @throws Exception
     */
    public static void mijing() throws Exception {
        Robot robot = new Robot();
        SecureRandom random = new SecureRandom();
        while (true) {
            try {
                logger.info("开始检测是是否有进入战斗按钮");
                String imagePath = OperationUtils.screenshot();
                String words = OcrUtil.getOcrResult(imagePath);
                //  boolean b = SelfOcrUtil.existKeyWord("是否继续抓鬼");
                boolean b = words.contains("进入战斗");
                //  boolean b = true;
                logger.info("查找结果：{}", b);
                if (b) {
                    OperationUtils.findAndClick("进入战斗", imagePath);
                }
                FileUtil.del(imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimeUnit.SECONDS.sleep(30);
            robot.mouseMove(random.nextInt(200), random.nextInt(500));
        }
    }
}
