/**
 *
 */
package com.service.eurekaclient.model;

import lombok.Data;

/**
 * @author Administrator
 */
public class Rectangle {
    /**
     * @ClassName:Rectangle
     * @Description:区域信息
     */
    private String id;

    private String paperTemplateId;
    // 开始坐标点
    private String rectangleStartPoint;

    private int rectangleWidth;

    private int rectangleHeight;

    private String testQuesBlockId;

    private String status;

    private String createTime;

    private String modifyTime;

    private String rectangleName;

    private String rectangleCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaperTemplateId() {
        return paperTemplateId;
    }

    public void setPaperTemplateId(String paperTemplateId) {
        this.paperTemplateId = paperTemplateId;
    }

    public String getRectangleStartPoint() {
        return rectangleStartPoint;
    }

    public void setRectangleStartPoint(String rectangleStartPoint) {
        this.rectangleStartPoint = rectangleStartPoint;
    }

    public int getRectangleWidth() {
        return rectangleWidth;
    }

    public void setRectangleWidth(int rectangleWidth) {
        this.rectangleWidth = rectangleWidth;
    }

    public int getRectangleHeight() {
        return rectangleHeight;
    }

    public void setRectangleHeight(int rectangleHeight) {
        this.rectangleHeight = rectangleHeight;
    }

    public String getTestQuesBlockId() {
        return testQuesBlockId;
    }

    public void setTestQuesBlockId(String testQuesBlockId) {
        this.testQuesBlockId = testQuesBlockId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRectangleName() {
        return rectangleName;
    }

    public void setRectangleName(String rectangleName) {
        this.rectangleName = rectangleName;
    }

    public String getRectangleCode() {
        return rectangleCode;
    }

    public void setRectangleCode(String rectangleCode) {
        this.rectangleCode = rectangleCode;
    }
}
