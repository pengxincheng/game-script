package com.pxc.game;

import com.pxc.game.ocr.SelfOcrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(3);


        //TimeUnit.SECONDS.sleep(5);
        //  OperationUtils.findAndClick("百度一下");

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                boolean b = SelfOcrUtil.existKeyWord(new File("image/screenCapture/1640225292938.png"), "百度一下");
                logger.info("查找结果：{}", b);
            });
        }

        TimeUnit.SECONDS.sleep(1000);
    }
}
