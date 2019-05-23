package com.service.eurekaclient.controller;

import com.service.eurekaclient.model.ImgCutInfo;
import com.service.eurekaclient.service.AsyncTaskConfig;
import com.service.eurekaclient.service.AsyncTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/image")
public class EurekaClientContrller {
    @Value("${wlxy.imgcutservice.realpath}")
    String realpath;
    @Autowired
    AsyncTaskConfig asyncTaskConfig;

    @Autowired
    AsyncTaskService asyncTaskService;

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncTaskConfig.class);

    @RequestMapping(value = "/cutImage", method = RequestMethod.POST)
    public boolean cutImage(@RequestBody(required = true) ImgCutInfo info) {
        try {
            // 做异步处理
            asyncTaskService.asyncInvokeReturnFuture(info);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}