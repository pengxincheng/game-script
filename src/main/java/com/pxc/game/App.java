package com.pxc.game;

import com.pxc.game.ocr.SelfOcrUtil;
import com.pxc.game.script.Script;
import com.pxc.game.util.OperationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception {
        logger.info("5秒后开始运行");
        TimeUnit.SECONDS.sleep(5);



           // Script.zhuaGui();
            Script.menPai();



    }
}
