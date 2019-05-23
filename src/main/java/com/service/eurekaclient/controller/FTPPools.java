package com.service.eurekaclient.controller;

import com.service.eurekaclient.ftp.FTPClientConfigure;
import com.service.eurekaclient.ftp.FTPClientPool;
import com.service.eurekaclient.ftp.FtpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

@Controller
public class FTPPools implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @Value("${FTP_CLIENTTIMEOUT}")
    private int FTP_CLIENTTIMEOUT;

    @Value("${FTP_PASSIVEMODE}")
    private String FTP_PASSIVEMODE;

    @Value("${FTP_ENCODING}")
    private String FTP_ENCODING;

    @Value("${FTP_THREADNUM}")
    private int FTP_THREADNUM;

    @Value("${FTP_TRANSFERFILETYPE}")
    private int FTP_TRANSFERFILETYPE;

    private static FTPClientPool pool;

    private volatile static FTPPools instance;

    @Override
    public void run(String... arg0) throws Exception {
        // 启动时，加载ftp连接池
        logger.debug("StartLoading：服务启动的时候运行，正在加载数据。。。。。");
        FTPClientConfigure ftpClientConfigure = new FTPClientConfigure(FTP_ADDRESS, FTP_PORT,
                FTP_USERNAME, FTP_PASSWORD,
                FTP_PASSIVEMODE, FTP_ENCODING,
                FTP_CLIENTTIMEOUT, FTP_THREADNUM,
                FTP_TRANSFERFILETYPE);

        FtpClientFactory factory = new FtpClientFactory(ftpClientConfigure);
        pool = new FTPClientPool(factory);
        System.out.println("StartLoading：服务启动的时候运行，正在加载数据。。。。。");
//        logger.debug("StartLoading：服务启动的时候运行，正在加载数据。。。。。");
    }

    private FTPPools() {
    }

    ;

    public static FTPPools getInstance() {
        synchronized (FTPPools.class) {
            if (instance == null) {
                instance = new FTPPools();
            }
        }
        return instance;
    }

    public FTPClientPool getPool() {
        return pool;
    }
}