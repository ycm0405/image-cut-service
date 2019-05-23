package com.service.eurekaclient.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author ycmdoing@163.com
 * @Description: 坐标
 * @date 2018年3月19日
 */
public class Coordinate {
    // 条形码识别结果
    private String barCode;
    // 模板id
    private String paperTemplateId;
    // 题块id
    private String testQuesBlockId;
    // 模板类型
    private int templateType;
    // 剪切试卷id
    private String id;
    // 切割保存路径
    private String destPath;
    //定位点占比
    private String areaProportion;
    //标志点类型
    private String type;
    //要截取得坐标位置列表
    private List<Point> pointList;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getPaperTemplateId() {
        return paperTemplateId;
    }

    public void setPaperTemplateId(String paperTemplateId) {
        this.paperTemplateId = paperTemplateId;
    }

    public String getTestQuesBlockId() {
        return testQuesBlockId;
    }

    public void setTestQuesBlockId(String testQuesBlockId) {
        this.testQuesBlockId = testQuesBlockId;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }

    public String getAreaProportion() {
        return areaProportion;
    }

    public void setAreaProportion(String areaProportion) {
        this.areaProportion = areaProportion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
}
