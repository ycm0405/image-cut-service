/**
 *
 */
package com.service.eurekaclient.model;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class Test {
    /**
     * @ClassName:Test
     * @Description:
     */
    public static void main(String[] args) throws IOException {
        //图片拼接
        BufferedImage bi1 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_393.png"));
        BufferedImage bi2 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_394.png"));
        BufferedImage bi3 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_395.png"));
        BufferedImage bi4 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_396.png"));
        BufferedImage bi5 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_393.png"));
        BufferedImage bi6 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_394.png"));
        BufferedImage bi7 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_395.png"));
        BufferedImage bi8 = ImageIO.read(new File("D:\\ideWorkSpace\\eureka-client\\src\\main\\resources\\static\\answer\\1\\21\\0001-ADF0812D356AA3AB509F0E362577A3F73\\153_396.png"));
        List<BufferedImage> list = new ArrayList<>();
        list.add(bi1);
        list.add(bi2);
        list.add(bi3);
        list.add(bi4);
        list.add(bi5);
        list.add(bi6);
        list.add(bi7);
        list.add(bi8);
        BufferedImage bufferedImage1 = stitchImgsByVertical(list);
        String savePath = "D:/abc.jpg";
        File outFile = new File(savePath);
        int location = savePath.lastIndexOf(".");
        String imageType = savePath.substring(location + 1, savePath.length());
        ImageIO.write(bufferedImage1, imageType, outFile);
    }

    /**
     * 垂直拼接多张图片
     *
     * @param bufferedImageList 要拼接的圖片
     * @return 拼接后圖片
     */
    public static BufferedImage stitchImgsByVertical(List<BufferedImage> bufferedImageList) {
        if (bufferedImageList == null || bufferedImageList.isEmpty()) {
            return null;
        }
        if (bufferedImageList.size() == 1) {
            return bufferedImageList.get(0);
        }
        BufferedImage returnBufferedImage = bufferedImageList.get(0);
        if (bufferedImageList.size() > 1) {
            for (int i = 1; i < bufferedImageList.size(); i++) {
                returnBufferedImage = stitchImgByVertical(returnBufferedImage, bufferedImageList.get(i));
            }
        }
        Point point = new Point(1, 1, 1, 1);
        List<Point> list = new ArrayList<>();
        list.add(point);
        Coordinate c1 = new Coordinate();
        c1.setBarCode("1");
        c1.setPointList(list);
        Coordinate c2 = new Coordinate();
        c2.setBarCode("2");
        c2.setPointList(list);
        Coordinate c3 = new Coordinate();
        c3.setPointList(list);
        c3.setBarCode("3");
        Coordinate c4 = new Coordinate();
        c4.setPointList(list);
        c4.setBarCode("4");
        List<Coordinate> lista = new ArrayList<>();
        lista.add(c1);
        lista.add(c2);

        List<Coordinate> listb = new ArrayList<>();
        listb.add(c3);
        listb.add(c4);
        lista.addAll(listb);
        File file = new File("src/main/resources/static/answer/1/21/0001-ADF0812D356AA3AB509F0E362577A3F73.jpg");

        if (file.exists()) {
            System.out.println("assssa");
        } else {
            System.out.println(file.getAbsolutePath());
        }
        return returnBufferedImage;
        //两个list求并集

    }

    /**
     * 纵向拼接图片
     *
     * @param imageFirst  第一張圖片
     * @param imageSecond 第二張圖片
     * @return BufferedImage null表示参入有误，或写入失败
     */
    public static BufferedImage stitchImgByVertical(BufferedImage imageFirst, BufferedImage imageSecond) {
        if (imageFirst == null || imageSecond == null) {
            return null;
        }
        BufferedImage imageResult = null;
        try {
            int width1 = imageFirst.getWidth();
            int height1 = imageFirst.getHeight();
            // 从图片中读取RGB
            int[] imageArrayFirst = new int[width1 * height1];
            imageArrayFirst = imageFirst.getRGB(0, 0, width1, height1, imageArrayFirst, 0, width1);

            int width2 = imageSecond.getWidth();
            int height2 = imageSecond.getHeight();
            int[] imageArraySecond = new int[width2 * height2];
            imageArraySecond = imageSecond.getRGB(0, 0, width2, height2, imageArraySecond, 0, width2);

            // 生成新图片,使用白色填充背景
            imageResult = new BufferedImage(Math.max(width1, width2), height1 + height2, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) imageResult.getGraphics();
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, width1 + width2, Math.max(height1, height2));
            g2.setPaint(Color.RED);

            imageResult.setRGB(0, 0, width1, height1, imageArrayFirst, 0, width1);
            imageResult.setRGB(0, height1, width2, height2, imageArraySecond, 0, width2);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return imageResult;
    }

    /**
     * 横向拼接图片
     *
     * @param imageFirst  第一張圖片
     * @param imageSecond 第二張圖片
     * @return BufferedImage null表示参入有误，或写入失败
     */
    public static BufferedImage stitchImgByHorizontal(BufferedImage imageFirst, BufferedImage imageSecond) {
        if (imageFirst == null || imageSecond == null) {
            return null;
        }
        BufferedImage imageResult = null;
        try {
            int width1 = imageFirst.getWidth();
            int height1 = imageFirst.getHeight();
            int[] imageArrayFirst = new int[width1 * height1];
            imageArrayFirst = imageFirst.getRGB(0, 0, width1, height1, imageArrayFirst, 0, width1);

            int width2 = imageSecond.getWidth();
            int height2 = imageSecond.getHeight();
            int[] imageArraySecond = new int[width2 * height2];
            imageArraySecond = imageSecond.getRGB(0, 0, width2, height2, imageArraySecond, 0, width2);

            // 生成新图片
            imageResult = new BufferedImage(width1 + width2, Math.max(height1, height2), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) imageResult.getGraphics();
            g2.setBackground(Color.WHITE);
            g2.clearRect(0, 0, width1 + width2, Math.max(height1, height2));
            g2.setPaint(Color.RED);
            imageResult.setRGB(0, 0, width1, height1, imageArrayFirst, 0, width1);
            imageResult.setRGB(width1, 0, width2, height2, imageArraySecond, 0, width2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return imageResult;
    }
}
