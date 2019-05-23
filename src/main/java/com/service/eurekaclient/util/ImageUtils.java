package com.service.eurekaclient.util;

import com.service.eurekaclient.controller.FTPPools;
import com.service.eurekaclient.model.Coordinate;
import com.service.eurekaclient.model.ImgType;
import com.service.eurekaclient.model.Point;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author ycmdoing@163.com
 * @Description: 图片处理类
 * @date 2018年3月19日
 */
@Component
public class ImageUtils {
    // ftp 服务器ip地址
    private static String FTP_ADDRESS;

    // ftp 服务器port，默认是21
    private static Integer FTP_PORT;

    // ftp 服务器密码
    private static String FTP_PASSWORD;

    // ftp 服务器存储图片的绝对路径
    private static String FTP_BASE_PATH;

    // ftp 服务器外网访问图片路径
    private static String IMAGE_BASE_URL;


    // ftp 服务器用户名
    private static String FTP_USERNAME;

    @Value("${FTP_ADDRESS}")
    public void setFTP_ADDRESS(String FTP_ADDRESS) {
        ImageUtils.FTP_ADDRESS = FTP_ADDRESS;
    }

    @Value("${FTP_PORT}")
    public void setFTP_PORT(Integer FTP_PORT) {
        ImageUtils.FTP_PORT = FTP_PORT;
    }

    @Value("${FTP_USERNAME}")
    public void setFTP_USERNAME(String FTP_USERNAME) {
        ImageUtils.FTP_USERNAME = FTP_USERNAME;
    }

    @Value("${FTP_PASSWORD}")
    public void setFTP_PASSWORD(String FTP_PASSWORD) {
        ImageUtils.FTP_PASSWORD = FTP_PASSWORD;
    }

    @Value("${FTP_BASE_PATH}")
    public void setFTP_BASE_PATH(String FTP_BASE_PATH) {
        ImageUtils.FTP_BASE_PATH = FTP_BASE_PATH;
    }

    @Value("${IMAGE_BASE_URL}")
    public void setIMAGE_BASE_URL(String IMAGE_BASE_URL) {
        ImageUtils.IMAGE_BASE_URL = IMAGE_BASE_URL;
    }


    /**
     * @param srcPath  图片地址,相对路径
     * @param point    坐標點
     * @param isWrite  剪切信息是否输出
     * @param destPath 输出路径
     * @return BufferedImage 返回类型
     * @Description: 根据坐标点剪切图片
     */
    public static BufferedImage cutImage(String srcPath, Point point,
                                         boolean isWrite, String destPath, ImgType type) {
        BufferedImage bi = null;
        String filePath = srcPath.substring(0, srcPath.lastIndexOf("/") + 1);
        String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
        try {
            String ext = srcPath.substring(srcPath.lastIndexOf(".") + 1);
            // ftp 获取图片
            InputStream in = downCutPic(filePath, fileName);
            ImageInputStream iis = ImageIO.createImageInputStream(in);
            //String picType = getPicType((FileInputStream) in);
            // 获取png图片的ImageReader的Iterator
            Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
            // 根据Iterator获取ImageReader
            ImageReader reader = (ImageReader) iterator.next();
            // 获取源图片输入流
            // 根据源图片输入流获得ImageInputStream流
            // 将ImageInputStream流加载到ImageReader中
            reader.setInput(iis, true);
            // 图片读取参数
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(point.getX(),
                    point.getY(), point.getWidth(),
                    point.getHeight());
            // 参数对象设置形状为一定大小的长方形
            param.setSourceRegion(rect);
            // ImageReader根据参数对象获得BufferedImage
            bi = reader.read(0, param);
            // 将经过参数对象筛选的图片流写入目标文件中
            if (isWrite) {
                File destFile = null;
                ImageIO.write(bi, type.getValue(), destFile);
                //destPath路径转化
                String destFilePath = srcPath.substring(0, srcPath.lastIndexOf("/") + 1);
                String destFileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
                uploadPicture(getMultFileFromFile(destFile).getInputStream(), destFilePath, destFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bi;
    }

    /**
     * @param bi 参数
     * @return void 返回类型
     * @Description: java内部实现的灰度化
     */
    public static BufferedImage grayImage(BufferedImage bi) {
        try {
            // 创建一个灰度模式的图片
            BufferedImage back = new BufferedImage(bi.getWidth(),
                    bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            int width = bi.getWidth();
            int height = bi.getHeight();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    back.setRGB(i, j, bi.getRGB(i, j));
                }
            }
            return back;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 权重灰度化
     *
     * @param bi 要灰度化圖片
     * @return 灰度化后的图片
     */
    public static BufferedImage grayImageByWeight(BufferedImage bi) {
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                final int color = bi.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                int newPixel = colorToRGB(255, gray, gray, gray);
                bi.setRGB(i, j, newPixel);
            }
        }
        return bi;
    }

    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

    /**
     * @param bi 参数
     * @return void 返回类型
     * @Description: 图片二值化
     */
    public static BufferedImage DipoleImage(BufferedImage bi) {
        int sWight = bi.getWidth();
        int sHight = bi.getHeight();
        BufferedImage newImage = new BufferedImage(sWight, sHight,
                BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < sWight; x++) {
            for (int y = 0; y < sHight; y++) {
                int rgb = bi.getRGB(x, y);
                newImage.setRGB(x, y, rgb);
            }
        }
        return newImage;
    }

    /**
     * 计算黑色在图片中的占比
     *
     * @param bi 包含黑色的图片
     * @throws Exception
     */
    public static double getBlackPixel(BufferedImage bi) {
        int blackValue = 0;
        int whiteValue = 0;
        int[] rgb = new int[3];
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        for (int i = minx; i < width; i++) {
            for (int j = miny; j < height; j++) {
                int pixel = bi.getRGB(i, j);
                /*
                 * Object data =bi.getRaster().getDataElements(i, j, null); int
                 * red = bi.getColorModel().getRed(data); int green =
                 * bi.getColorModel().getGreen(data); int blue =
                 * bi.getColorModel().getBlue(data); System.out.println("(" +
                 * red + "," + green + "," + blue + ")");
                 */

                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                /*
                 * int r = (rgb[0] >> 16) & 0xff; int g = (rgb[1] >> 8) & 0xff;
                 * int b = rgb[2] & 0xff;
                 *
                 * System.out.println("(" + r + "," + g + "," + b + ")");
                 */
                if (rgb[0] > 200 && rgb[1] > 200 && rgb[2] > 200) {
                    whiteValue++;
                } else {
                    blackValue++;
                }

            }
        }
        BigDecimal b1 = new BigDecimal(Double.toString(blackValue));
        BigDecimal b2 = new BigDecimal(Double.toString(whiteValue + blackValue));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 判断图片的边界是否存在黑色
     */
    public static boolean borderBlack(BufferedImage bi) {
        // 1、获取图片的四条边的坐标
        int width = bi.getWidth();
        int height = bi.getHeight();
        // 顺时针四条边（width.range,0）(width.max,height.range)(width.range,height.max)(0,height.range)

        // 2、对边上的坐标取点，进行颜色判断
        for (int i = 0; i < width; i++) {
            if (isBlack(bi, i, 0)) {
                return true;
            }
        }
        for (int j = 0; j < height; j++) {
            if (isBlack(bi, width - 1, j)) {
                return true;
            }
        }

        for (int i = 0; i < width; i++) {
            if (isBlack(bi, i, height - 1)) {
                return true;
            }
        }

        for (int j = 0; j < height; j++) {
            if (isBlack(bi, 0, j)) {
                return true;
            }
        }
        // 3、不存在，返回false
        return false;
    }

    /**
     * @param @param  bi
     * @param @param  i 坐标x
     * @param @param  j 坐标y
     * @param @return 参数
     * @return boolean 返回类型
     * @Description: 判断图片中的点是否为黑色
     */
    public static boolean isBlack(BufferedImage bi, int i, int j) {
        int pixel = bi.getRGB(i, j);
        int[] rgb = new int[3];
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
        if (rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0) {
            return true;
        }
        return false;
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

    /**
     * 判断试卷是否规则
     *
     * @param srcPath   图片路径
     * @param list      标志点集合
     * @param threshold 截取标志点在位置中的阈值
     * @param isWrite   是否输出截图后灰度化图片
     * @param destPath  生成路径
     * @param type      生成图片类型
     * @return
     */
    public static boolean isRegularExam(String srcPath, List<Coordinate> list,
                                        double threshold, boolean isWrite, String destPath, ImgType type) {

        for (int i = 0; i < list.size(); i++) {
            Coordinate coordinate = list.get(i);
            // 1、坐标点剪切
            BufferedImage cutImage = ImageUtils.cutImage(srcPath, coordinate.getPointList().get(0),
                    false, "", ImgType.PNG);
            // 2、灰度化
            BufferedImage grayImage = ImageUtils.grayImage(cutImage);
            if (isWrite) {
                try {
                    ImageIO.write(grayImage, type.getValue(), new File(destPath
                            + i + "." + type.getValue()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 3、灰度化后图片四边判断
            boolean borderBlack = ImageUtils.borderBlack(grayImage);
            // 4、如果为四边中包括黑色，面积占比>阈值；不包括黑色切中间有黑色，试卷位置规则
            double imagePixel = ImageUtils.getBlackPixel(grayImage);
            if (borderBlack && imagePixel <= threshold) {
                return false;
            }
            if (!borderBlack && imagePixel <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从试卷获取考生ID
     *
     * @param imgPath 原始试卷路径
     * @param point   识别区坐标
     * @return 0:未能识别，其他值：识别正确
     */
    public static String getIDFromPaper(String imgPath,
                                        Point point) {
        BufferedImage cutImage = cutImage(imgPath, point, false,
                "", ImgType.PNG);
        BufferedImage grayImage = ImageUtils.grayImage(cutImage);
        String id = BarCodeUtils.decode(grayImage);
        return id;
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
        return returnBufferedImage;
    }

    /**
     * file转为MultipartFile
     *
     * @param file
     * @return
     */
    public static MultipartFile getMultFileFromFile(File file) {
        try {
            // File转换成MutipartFile
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
            //注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则
            //MultipartFile.getName()只能拿到name参数，但是originalFilename是空。
            return multipartFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static boolean uploadPicture(InputStream inputStream, String filePath, String fileName) throws Exception {
        boolean result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                inputStream, filePath, fileName);
        return result;
    }

    public static InputStream downCutPic(String filePath, String fileName) throws Exception {

        return downloadCutFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, filePath, fileName);
    }

    public static InputStream downloadCutFile(String ip, Integer port, String account, String passwd,
                                              String filePath, String fileName) throws Exception {
        FTPClient ftpClient = FTPPools.getInstance().getPool().borrowObject();
        InputStream in = null;
        try {
            //验证是否有该文件夹，有就转到，没有创建后转到该目录下
            if (filePath != null) {
                ftpClient.changeWorkingDirectory(FTP_BASE_PATH);
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(filePath);
            String[] rt = ftpClient.doCommandAsStrings("pwd", "");
            Pattern p = Pattern.compile("\"(.*?)\"");
            Matcher m = p.matcher(rt[0]);
            if (m.find()) {
                System.out.println(m.group(0).replace("\"", ""));
            }
            String remoteAbsoluteFile = toFtpFilename(fileName);
            // 下载文件
            ftpClient.setBufferSize(1024);
            in = ftpClient.retrieveFileStream(remoteAbsoluteFile);
            FTPPools.getInstance().getPool().returnObject(ftpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    public static boolean uploadFile(String ip, Integer port, String account, String passwd,
                                     InputStream inputStream, String workingDir, String fileName) throws Exception {
        boolean result = false;
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = FTPPools.getInstance().getPool().borrowObject();
        try {
            //切换到相对路径
            ftpClient.changeWorkingDirectory(FTP_BASE_PATH);
            String[] ftppaths = workingDir.split("/");
            for (int i = 0; i < ftppaths.length; i++) {
                boolean reulst = ftpClient.changeWorkingDirectory(ftppaths[i]);
                if (!reulst) {
                    ftpClient.makeDirectory(ftppaths[i]);
                    ftpClient.changeWorkingDirectory(ftppaths[i]);
                }
            }
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            // 6. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            if (!ftpClient.storeFile(new String(fileName.getBytes("UTF-8"), "iso-8859-1"), inputStream)) {
                return result;
            }
            // 8. 关闭连接
            inputStream.close();
            result = true;
            FTPPools.getInstance().getPool().returnObject(ftpClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[inStream.available()];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        swapStream.close();
        return in2b;
    }

    private static String toFtpFilename(String fileName) throws Exception {
        return new String(fileName.getBytes("GBK"), "ISO8859-1");
    }

    public static String getPicType(FileInputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return "jpg";
            } else if (type.contains("89504E47")) {
                return "gif";
            } else if (type.contains("47494638")) {
                return "png";
            } else if (type.contains("424D")) {
                return "bmp";
            } else {
                return "unknown";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
