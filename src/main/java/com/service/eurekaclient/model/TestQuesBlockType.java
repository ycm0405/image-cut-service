package com.service.eurekaclient.model;

/**
 * @author ycmdoing@163.com
 * @Description: 标志类型
 * @date 2018年3月21日
 */

public enum TestQuesBlockType {
    // 1：标志点
    IDENTIFY(1),
    // 2:条形码
    BARCODE(2),
    // 3:答题区
    ANSWERAREA(3);

    private int value = 1;

    private TestQuesBlockType(int value) {
        this.value = value;
    }

    public static TestQuesBlockType valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
            case 1:
                return IDENTIFY;
            case 2:
                return BARCODE;
            case 23:
                return ANSWERAREA;
            default:
                return null;
        }
    }

    public int value() {
        return this.value;
    }
}

