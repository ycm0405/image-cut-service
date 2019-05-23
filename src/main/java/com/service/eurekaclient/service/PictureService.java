package com.service.eurekaclient.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface PictureService {

    /**
     * 上传文件到ftp
     *
     * @param uploadFile 上传文件
     * @param filePath   上传的相对路径
     * @param fileName   文件名
     * @return 是否上传成功
     * @throws Exception
     */
    public boolean uploadPicture(MultipartFile uploadFile, String filePath, String fileName) throws Exception;

    /**
     * 下载图片文件为base64
     *
     * @param filePath
     * @return base64信息
     */
    public String downPicture(String filePath) throws Exception;

    /**
     * ftp下载图片
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public File downCutPic(String filePath, String fileName) throws Exception;
}
