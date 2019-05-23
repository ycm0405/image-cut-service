package com.service.eurekaclient.service;

import com.service.eurekaclient.model.*;
import com.service.eurekaclient.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
// 线程执行任务类
public class AsyncTaskService {
    @Value("${wlxy.imgcutservice.realpath}")
    String realpath;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    @Async
    public void executeAsyncTask(Integer i) {
        System.out.println("执行异步任务：" + i);
    }

    /**
     * 异常调用返回Future
     *
     * @param
     * @return
     * @throws InterruptedException
     */
    @Async
    public void asyncInvokeReturnFuture(ImgCutInfo info) throws InterruptedException {
        List<ImgCutResult> imgCutResultList = cutImage(info);
        sendCutResultMsg(imgCutResultList, info);
    }

    private void sendCutResultMsg(List<ImgCutResult> imgCutResultList, ImgCutInfo info) throws InterruptedException {
        ServiceInstance serviceInstance = loadBalancerClient.choose("EUREKA-CONSUMER");
        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/api/dealCutResult/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<ImgCutResult>> entity = new HttpEntity<>(imgCutResultList, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        System.out.println(responseEntity);
    }

    public List<ImgCutResult> cutImage(ImgCutInfo info) {
        if (info == null) {
            return new ArrayList<ImgCutResult>();
        }
        List<ImgCutResult> resultList = new ArrayList<ImgCutResult>();
        // 1、获取相关坐标点
        // 固定坐标点
        List<Coordinate> fixedCoordinates = new ArrayList<Coordinate>();
        // 条形码坐标点
        Coordinate barCodeConCoordinate = null;
        // 试题坐标点8
        List<Coordinate> examCoordinates = new ArrayList<Coordinate>();
        PaperTemplate paperTemplateList = info.getPaperTemplateList();
        List<TestQuesBlock> testQuesBlockList = paperTemplateList
                .getTestQuesBlockList();
        Coordinate coordinate = null;
        //每个要截取得块，有三种类型，条形码，标志点，答题区
        for (TestQuesBlock testQuesBlock : testQuesBlockList) {
            //答题区同一个块下面可能需要截取两部分，拼接成一个图片
            List<Rectangle> rectangleList = testQuesBlock.getRectangleList();
            Rectangle rectangle = rectangleList.get(0);
            String[] startPoint = rectangle.getRectangleStartPoint().split(",");
            // 获取定位点
            List<Point> points = new ArrayList<Point>();
            Point point = new Point(Integer.parseInt(startPoint[0]),
                    Integer.parseInt(startPoint[1]),
                    rectangle.getRectangleWidth(),
                    rectangle.getRectangleHeight());
            points.add(point);
            coordinate = new Coordinate();
            coordinate.setPointList(points);

            if (TestQuesBlockType.IDENTIFY.value() == testQuesBlock
                    .getTestQuesBlockType()) {
                coordinate.setType(String.valueOf(TestQuesBlockType.IDENTIFY.value()));
                fixedCoordinates.add(coordinate);
            }
            // 类型为2，條形碼坐標
            if (TestQuesBlockType.BARCODE.value() == testQuesBlock
                    .getTestQuesBlockType()) {
                coordinate.setType(String.valueOf(TestQuesBlockType.BARCODE.value()));
                barCodeConCoordinate = coordinate;
            }
            // 类型为答题区
            if (TestQuesBlockType.ANSWERAREA.value() == testQuesBlock
                    .getTestQuesBlockType()) {
                // 记录相关信息
                List<Point> answerPoints = new ArrayList<>();
                for (Rectangle examRectangle :
                        rectangleList) {
                    String[] examArray = examRectangle.getRectangleStartPoint().split(",");
                    Point answerPoint = new Point(Integer.parseInt(examArray[0]),
                            Integer.parseInt(examArray[1]),
                            examRectangle.getRectangleWidth(),
                            examRectangle.getRectangleHeight());
                    answerPoints.add(answerPoint);
                }
                //设置剪切列表
                coordinate.setPointList(answerPoints);
                coordinate.setType(String.valueOf(TestQuesBlockType.ANSWERAREA.value()));
                coordinate.setTestQuesBlockId(testQuesBlock.getId());
                coordinate.setPaperTemplateId(paperTemplateList.getId());
                coordinate.setTemplateType(paperTemplateList.getTemplateType());
                examCoordinates.add(coordinate);
            }
        }


        // 2、获取试卷路径
        List<AnswerPaper> answerPaperList = info.getAnswerPaperList();
        for (AnswerPaper answerPaper : answerPaperList) {
            // 1、获取相关坐标点
            // 固定坐标点
            List<Coordinate> fixedCoordinatesInsert = new ArrayList<Coordinate>();
            // 条形码坐标点
            Coordinate barCodeConCoordinateInsert = new Coordinate();
            barCodeConCoordinateInsert.setType("2");
            // 试题坐标点8
            List<Coordinate> examCoordinatesInsert = new ArrayList<Coordinate>();
            ImgCutResult result = new ImgCutResult();
            // 3、对试卷是否规正判定
            String uploadPath = answerPaper.getUploadPath();
            String uploadFileName = answerPaper.getUploadFileName();
            String srcPath = uploadPath + uploadFileName;
           /* if (!new File(srcPath).isFile()) {
                result.setAnswerPaperId(answerPaper.getAnswerPaperId());
                result.setCutResult(-1);
                result.setPaperTemplateId(answerPaper.getPaperTemplateId());
                result.setExamId(info.getExamId());
                result.setRoomId(info.getRoomId());
                result.setDesc("img cannot be identified because of Not Found");
                resultList.add(result);
                continue;
            }*/

            for (Coordinate pointCoordinate : fixedCoordinates) {
                Coordinate pointCoordinateInsert = new Coordinate();
                //计算试卷中标志点的占比
                BufferedImage cutBufferedImage = ImageUtils.cutImage(srcPath, pointCoordinate.getPointList().get(0), false, "", ImgType.PNG);
                BufferedImage grayBufferedImage = ImageUtils.grayImageByWeight(cutBufferedImage);
                double blackPixel = ImageUtils.getBlackPixel(grayBufferedImage);
                //设值
                pointCoordinateInsert.setAreaProportion(String.valueOf(blackPixel));
                pointCoordinateInsert.setType(String.valueOf(TestQuesBlockType.IDENTIFY.value()));
                fixedCoordinatesInsert.add(pointCoordinateInsert);
            }


            // 4、识别考生id
            String barCode = "0";
            //正面识别条形码
            if (barCodeConCoordinate != null) {
                barCode = ImageUtils.getIDFromPaper(srcPath,
                        barCodeConCoordinate.getPointList().get(0));
                barCodeConCoordinateInsert.setBarCode(barCode);
                barCodeConCoordinateInsert.setType(String.valueOf(TestQuesBlockType.BARCODE.value()));
            }
            // 5、对试卷分割并生成文件
            for (Coordinate examCoordinate : examCoordinates) {
                Coordinate examCoordinateInsert = new Coordinate();
                // 获取文件生成路径,文件命名格式answerPaperId_testQuesBlockId.png,
                String fileName = uploadFileName.substring(0,
                        uploadFileName.lastIndexOf("."));
                String destFile = answerPaper.getUploadPath() + fileName
                        + "/";

                String destPath = answerPaper.getUploadPath() + fileName
                        + "/" + answerPaper.getAnswerPaperId() + "_"
                        + examCoordinate.getTestQuesBlockId() + "." + ImgType.PNG.value;
                File file = new File(destFile);
               /* if (!file.exists()) {
                    file.mkdirs();
                }*/
               /* if (new File(destPath).exists()) {
                    //获得当前时间的毫秒数
                    long t = System.currentTimeMillis();
                    Random rd = new Random(t);
                    destPath = destPath.substring(0, destPath.lastIndexOf(".")) + "_" + rd.nextInt(1000) + "." + ImgType.PNG.value;
                }*/
                List<Point> pointList = examCoordinate.getPointList();
                List<BufferedImage> bufferedImageList = new ArrayList<BufferedImage>();
                for (Point point : pointList) {
                    BufferedImage cutBufferedImage = ImageUtils.cutImage(srcPath, point, false, destPath,
                            ImgType.PNG);
                    bufferedImageList.add(cutBufferedImage);
                }
                BufferedImage resultBufferImage = ImageUtils.stitchImgsByVertical(bufferedImageList);
                try {
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
                    //ImageIO.write(resultBufferImage, "png", imOut);
                    InputStream is = new ByteArrayInputStream(bs.toByteArray());
                    //ImageIO.write(resultBufferImage, ImgType.PNG.value, new File(realpath + destPath));
                    String destFilePath = destPath.substring(0, destPath.lastIndexOf("/") + 1);
                    String destFileName = destPath.substring(destPath.lastIndexOf("/") + 1);
                    ImageUtils.uploadPicture(is, destFilePath, destFileName);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                examCoordinateInsert.setTestQuesBlockId(examCoordinate.getTestQuesBlockId());
                examCoordinateInsert.setDestPath(destPath);
                examCoordinateInsert.setBarCode(barCode);
                examCoordinateInsert.setType(String.valueOf(TestQuesBlockType.ANSWERAREA.value()));
                examCoordinatesInsert.add(examCoordinateInsert);
            }

            if (barCodeConCoordinateInsert != null) {
                fixedCoordinatesInsert.add(barCodeConCoordinateInsert);
                fixedCoordinatesInsert.addAll(examCoordinatesInsert);
            } else {
                fixedCoordinatesInsert.addAll(examCoordinatesInsert);
            }


            result.setList(fixedCoordinatesInsert);
            result.setAnswerPaperId(answerPaper.getAnswerPaperId());
            result.setUserId(barCode);
            result.setCutResult(0);
            result.setPaperTemplateId(answerPaper.getPaperTemplateId());
            result.setExamId(info.getExamId());
            result.setRoomId(info.getRoomId());
            result.setDesc("img cut success");
            resultList.add(result);
        }
        return resultList;
    }
}