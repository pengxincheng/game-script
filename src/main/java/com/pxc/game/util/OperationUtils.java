package com.pxc.game.util;

import com.pxc.game.model.Location;
import com.pxc.game.ocr.OcrUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author: pengxincheng
 * @Date: 2019/12/5 13:59
 * @Description:
 */
public class OperationUtils {

    /**
     * 截屏，返回图片路径
     *
     * @return
     */
    public static String screenshot() throws IOException, AWTException {
        // 获取屏幕的尺寸
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("The width and the height of the screen are " + screenSize.getWidth() + " x " + screenSize.getHeight());
        // 截取屏幕
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(screenSize));
        // 创建一个用于保存图片的文件夹
        String path = "D:/test/screenCapture/";
        File screenCaptureDirectory = new File(path);
        if (!screenCaptureDirectory.exists()) {
            screenCaptureDirectory.mkdirs();
        }
        String imageName = System.currentTimeMillis() + ".png";
        File imageFile = new File(path, imageName);
        // 以指定的图片格式将截取的图片写到指定的文件
        ImageIO.write(image, "png", imageFile);
        return path + imageName;
    }

    /**
     * 找文字位置
     * @param filePath
     * @param keyWord
     * @return
     */
    public static Location getWordLocation(String filePath, String keyWord) {
        return OcrUtil.findCoordinate(filePath, keyWord);
    }

    /**
     * 找并且点击
     * @param keyWord
     * @throws IOException
     * @throws AWTException
     */
    public static void findAndClick(String keyWord) throws IOException, AWTException {
        String imgPath = screenshot();
        Location location = getWordLocation(imgPath, keyWord);

        Robot robot = new Robot();
        robot.delay(1000);

        robot.mouseMove(location.getX()+10, location.getY()+10);
        robot.delay(1000);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(1000);

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}
