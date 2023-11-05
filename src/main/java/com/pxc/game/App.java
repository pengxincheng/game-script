package com.pxc.game;

import com.pxc.game.script.FuBenScript;
import com.pxc.game.script.HaidiScript;
import com.pxc.game.script.MiJingScript;
import com.pxc.game.script.Script;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception {
        logger.info("3秒后开始运行");
         TimeUnit.SECONDS.sleep(3);

      //   Script.zhuaGui();
       // Script.menPai();

      //  MiJingScript.mijing();

     //   HaidiScript.haidi();


        yitiao();


    }


    // 三本 + 鬼
    public static void yitiao()throws Exception{
        String fubenType = "侠士副本";
        Boolean left = true;
        FuBenScript.start(fubenType,left);

        String fubenType1 = "普通副本";
        Boolean left1 = true;
        FuBenScript.start(fubenType1,left1);

        String fubenType2 = "普通副本";
        Boolean left2 = false;
        FuBenScript.start(fubenType2,left2);

        OperationUtils.clickHuoDong("鬼任务");
        TimeUnit.SECONDS.sleep(10);

        String taskImagePath = OperationUtils.screenshot();
        OperationUtils.findAndClick("捉鬼任务", taskImagePath);
        TimeUnit.SECONDS.sleep(3);

        String zhuaguiImagePath = OperationUtils.screenshot();
        OperationUtils.findAndClick("1/10", zhuaguiImagePath);
        TimeUnit.SECONDS.sleep(3);

        Robot robot = new Robot();
        robot.delay(1000);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        Script.zhuaGui();
    }
}
