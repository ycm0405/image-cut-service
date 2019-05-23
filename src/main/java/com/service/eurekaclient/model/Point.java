package com.service.eurekaclient.model;

import lombok.Data;

/**
 * 截取坐标点
 */
public class Point {
    // 起始坐标x
    private int x;
    // 起始坐标y
    private int y;
    // 截取长度
    private int width;
    // 截取宽度
    private int height;

    public int getX() {
        return x;
    }

    public Point(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
