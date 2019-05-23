package com.service.eurekaclient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 试卷所有的定位点类
 */
public class MarkCoordinates {
    // 固定坐标点
    List<Coordinate> fixedCoordinates = new ArrayList<Coordinate>();
    // 条形码坐标点
    Coordinate barCodeConCoordinate = null;
    // 试题坐标点8
    List<Coordinate> examCoordinates = new ArrayList<Coordinate>();

    public List<Coordinate> getFixedCoordinates() {
        return fixedCoordinates;
    }

    public void setFixedCoordinates(List<Coordinate> fixedCoordinates) {
        this.fixedCoordinates = fixedCoordinates;
    }

    public Coordinate getBarCodeConCoordinate() {
        return barCodeConCoordinate;
    }

    public void setBarCodeConCoordinate(Coordinate barCodeConCoordinate) {
        this.barCodeConCoordinate = barCodeConCoordinate;
    }

    public List<Coordinate> getExamCoordinates() {
        return examCoordinates;
    }

    public void setExamCoordinates(List<Coordinate> examCoordinates) {
        this.examCoordinates = examCoordinates;
    }
}
