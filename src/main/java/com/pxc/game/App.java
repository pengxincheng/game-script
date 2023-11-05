package com.pxc.game;

import com.pxc.game.script.HaidiScript;
import com.pxc.game.script.MiJingScript;
import com.pxc.game.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception {
        logger.info("10秒后开始运行");
         TimeUnit.SECONDS.sleep(10);

         Script.zhuaGui();
       // Script.menPai();

      //  MiJingScript.mijing();

     //   HaidiScript.haidi();

    }
}
