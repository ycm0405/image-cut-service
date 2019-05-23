package com.service.eurekaclient.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.service.eurekaclient.model.ImgType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author ycmdoing@163.com
 * @Description: 一维码生成与解析
 * @date 2018年3月21日
 */

public class BarCodeUtils {
    /**
     * zxing条形码编码
     *
     * @param contents 内容
     * @param width    图片的宽度
     * @param height   图片高度
     * @param format  编码格式 BarcodeFormat枚举类
     * @param imgPath  生成图片路径
     */
    public static void encode(String contents, int width, int height,
                              String imgPath, BarcodeFormat format, ImgType type) {
        // 保证最小为70*25的大小
        int codeWidth = Math.max(70, width);
        int codeHeight = Math.max(25, height);
        try {
            // 使用EAN_13编码格式进行编码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    format, codeWidth, codeHeight, null);
            // 生成png格式的图片保存到imgPath路径
            MatrixToImageWriter.writeToStream(bitMatrix, type.getValue(),
                    new FileOutputStream(imgPath));
            System.out.println("encode success! the img's path is " + imgPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * zxing解析条形码
     *
     * @param imgPath
     * @return
     */
    public static String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * zxing解析条形码
     *
     * @param image 含有条形码的图片
     * @return
     */
    public static String decode(BufferedImage image) {
        Result result = null;
        if (image == null) {
            System.out.println("the decode image may be not exit.");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            result = new MultiFormatReader().decode(bitmap, null);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
        return result.getText();
    }

    /**
     * 根据图片类型保存图片
     *
     * @param paramBufferedImage 图片流
     * @param destPath            保存路径
     * @param type               图片类型，枚举类ImgType
     */
    public static void saveImgByType(BufferedImage paramBufferedImage,
                                     String destPath, ImgType type) {
        try {
            ImageIO.write(paramBufferedImage, type.getValue(), new File(
                    destPath));
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
