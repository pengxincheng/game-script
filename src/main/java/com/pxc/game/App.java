package com.pxc.game;

import com.pxc.game.util.OperationUtils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {


        TimeUnit.SECONDS.sleep(5);
        OperationUtils.findAndClick("百度一下");


    }
}
