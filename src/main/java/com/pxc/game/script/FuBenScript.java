package com.pxc.game.script;

import cn.hutool.core.io.FileUtil;
import com.pxc.game.model.Location;
import com.pxc.game.ocr.OcrUtil;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author pengxincheng
 * @Date 2022/12/7 20:57
 * @Description
 */
public class FuBenScript {
    private static final Logger logger = LoggerFactory.getLogger(FuBenScript.class);

    /**
     * 副本
     *
     * @throws Exception
     */
    public static void start(String fubenType,Boolean left) throws Exception {

        if(Objects.equals("普通副本",fubenType)) {
            OperationUtils.clickHuoDong("普通");

            TimeUnit.SECONDS.sleep(3);

            String xuanZeFuBenPath = OperationUtils.screenshot();

            OperationUtils.findAndClick("选择副本", xuanZeFuBenPath);


            TimeUnit.SECONDS.sleep(3);
            String putongPaht = OperationUtils.screenshot();
            OperationUtils.findAndClick("普通副本", putongPaht);

            TimeUnit.SECONDS.sleep(1);
            String jinruPath = OperationUtils.screenshot();
            if(left) {
                OperationUtils.findAndClickLeft("进入", jinruPath);
            }else {
                OperationUtils.findAndClick("进入", jinruPath);
            }
        }

        while (true){

            TimeUnit.SECONDS.sleep(5);
            String donghuaPaht = OperationUtils.screenshot();
            String res = OcrUtil.getOcrResult(donghuaPaht);
            if(res.contains("跳过剧情动画")){
                OperationUtils.findAndClick("跳过剧情动画", donghuaPaht);
                TimeUnit.SECONDS.sleep(3);
            }

            TimeUnit.SECONDS.sleep(1);
            String zhandouPath = OperationUtils.screenshot();
            String zhdou = OcrUtil.getOcrResult(zhandouPath);
            if(zhdou.contains("普通")){
                OperationUtils.findAndClick("普通", zhandouPath);
                TimeUnit.SECONDS.sleep(5);

                String keyiPath = OperationUtils.screenshot();
                String keyi = OcrUtil.getOcrResult(keyiPath);

               click(keyi,keyiPath);

            }

            if(zhdou.contains("侠士")){


                OperationUtils.findAndClick("侠士", zhandouPath);
                TimeUnit.SECONDS.sleep(3);

                String path = OperationUtils.screenshot();
                String msg = OcrUtil.getOcrResult(path);

                click(msg,path);

            }

            if(zhdou.contains("长安城")){

                logger.info("当前副本已经完成");
                break;
            }

            TimeUnit.SECONDS.sleep(10);
        }
    }


    private static void click(String word,String path)throws Exception{
        // 琉璃副本
        if(word.contains("当然可以")){
            OperationUtils.findAndClick("当然可以",path);
            TimeUnit.SECONDS.sleep(2);
        }

//                if(keyi.contains("点击任意位置")){
//
//                    TimeUnit.SECONDS.sleep(1);
//                }
        if(word.contains("进入战斗")){
            OperationUtils.findAndClick("进入战斗",path);
            TimeUnit.SECONDS.sleep(2);
        }


        // 淘海去副本
        if(word.contains("竖子尔敢")){
            OperationUtils.findAndClick("竖子尔敢",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("恕难从命")){
            OperationUtils.findAndClick("恕难从命",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("恕难从命")){
            OperationUtils.findAndClick("恕难从命",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("与尔一战")){
            OperationUtils.findAndClick("与尔一战",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("陷入梦")){
            OperationUtils.findAndClick("陷入梦",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("海盗受死")){
            OperationUtils.findAndClick("海盗受死",path);
            TimeUnit.SECONDS.sleep(2);
        }

        if(word.contains("请选择要做的事")){
            Location location =  OperationUtils.getWordLocation(path,"请选择要做的事");

            Robot robot = new Robot();
            robot.delay(1000);

            robot.mouseMove(location.getX(),location.getY()+60);

            robot.delay(500);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(300);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
    }
}
