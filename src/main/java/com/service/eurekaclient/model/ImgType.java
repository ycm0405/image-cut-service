package com.service.eurekaclient.model;

/**
 * @author ycmdoing@163.com
 * @Description: 图片类型
 * @date 2018年3月21日
 */

public enum ImgType {
    // 不需要输出
    NULL(""),
    /**
     * 图片格式：.gif
     */
    GIF("gif"),
    /**
     * 图片格式：.png
     */
    PNG("png"),
    /**
     * 图片格式：.jpg
     */
    JPG("jpg"),
    /**
     * 图片格式：.jpeg
     */
    JPEG("jpeg"),;

    ImgType(String value) {
        this.value = value;
    }

    public final String value;

    public String getValue() {
        return value;
    }
}
