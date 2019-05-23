package com.service.eurekaclient.service.impl;

import com.service.eurekaclient.service.PictureService;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.net.TelnetInputStream;

import java.io.*;


@Service
public class PictureServiceImpl implements PictureService {

    // ftp 服务器ip地址
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;

    // ftp 服务器port，默认是21
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;

    // ftp 服务器用户名
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;

    // ftp 服务器密码
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;

    @Value("${FTP_BASE_PATH}")
    // ftp 服务器存储图片的绝对路径
    private String FTP_BASE_PATH;

    // ftp 服务器外网访问图片路径
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public boolean uploadPicture(MultipartFile uploadFile, String filePath, String fileName) throws Exception {
        boolean result = uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD,
                uploadFile.getInputStream(), filePath, fileName);
        return result;
    }

    @Override
    public String downPicture(String filePath) throws Exception {
        return downloadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, filePath);
    }

    @Override
    public File downCutPic(String filePath, String fileName) throws Exception {

        return downloadCutFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, filePath, fileName);
    }

    /**
     * ftp 上传图片方法
     *
     * @param ip          ftp 服务器ip地址
     * @param port        ftp 服务器port，默认是21
     * @param account     ftp 服务器用户名
     * @param passwd      ftp 服务器密码
     * @param inputStream 文件流
     * @param workingDir  ftp 服务器存储图片的绝对路径
     * @param fileName    上传到ftp 服务器文件名
     * @throws Exception
     */
    public boolean uploadFile(String ip, Integer port, String account, String passwd,
                              InputStream inputStream, String workingDir, String fileName) throws Exception {
        boolean result = false;
        // 1. 创建一个FtpClient对象
        FTPClient ftpClient = new FTPClient();
        try {
            // 2. 创建 ftp 连接
            ftpClient.connect(ip, port);
            // 3. 登录 ftp 服务器
            ftpClient.login(account, passwd);
            int reply = ftpClient.getReplyCode(); // 获取连接ftp 状态返回值
            System.out.println("code : " + reply);
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect(); // 如果返回状态不再 200 ~ 300 则认为连接失败
                return result;
            }
            // 4. 设置上传的路径
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
            // 5. 修改上传文件的格式为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            // 6. 服务器存储文件，第一个参数是存储在服务器的文件名，第二个参数是文件流
            if (!ftpClient.storeFile(new String(fileName.getBytes("UTF-8"), "iso-8859-1"), inputStream)) {
                return result;
            }
            // 8. 关闭连接
            inputStream.close();
            ftpClient.logout();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // FIXME 听说，项目里面最好少用try catch 捕获异常，这样会导致Spring的事务回滚出问题？？？难道之前写的代码都是假代码！！！
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public String downloadFile(String ip, Integer port, String account, String passwd,
                               String filePath) throws Exception {
        byte[] bytes = null;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            ftpClient.login(account, passwd);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            //验证是否有该文件夹，有就转到，没有创建后转到该目录下
            if (filePath != null) {
                ftpClient.changeWorkingDirectory(filePath);
            }
            String remoteAbsoluteFile = toFtpFilename(filePath);
            InputStream in = null;
            // 下载文件
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            in = ftpClient.retrieveFileStream(remoteAbsoluteFile);
            bytes = input2byte(in);
            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 文件转成 byte[] <一句话功能简述> <功能详细描述>
     *
     * @param inStream
     * @return
     * @throws IOException
     * @see [类、类#方法、类#成员]
     */
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

    /**
     * 转化输出的编码
     */
    private static String toFtpFilename(String fileName) throws Exception {
        return new String(fileName.getBytes("GBK"), "ISO8859-1");
    }

    public File downloadCutFile(String ip, Integer port, String account, String passwd,
                                String filePath, String fileName) throws Exception {
        File localFile = null;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip, port);
            ftpClient.login(account, passwd);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            //验证是否有该文件夹，有就转到，没有创建后转到该目录下
            if (filePath != null) {
                ftpClient.changeWorkingDirectory(filePath);
            }
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(filePath);

            localFile = new File(filePath + File.separatorChar + fileName);
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(fileName, os);
            os.close();
            ftpClient.logout();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return localFile;
    }
}
