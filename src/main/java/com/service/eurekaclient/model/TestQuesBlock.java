/**
 *
 */
package com.service.eurekaclient.model;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
public class TestQuesBlock {
    /**
     * @ClassName:TestQuesBlock
     * @Description:
     */
    // 截取块的标志
    private String id;
    // 模板id
    private String paperTemplateId;
    // 要裁剪区域的类型，分别为1：客观题 2：条形码 3：主观题
    private int testQuesBlockType;

    private String testQuesBlockName;

    private String status;

    private String createTime;

    private String modifyTime;

    private String testQuesBlockCode;

    private List<Rectangle> rectangleList;

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

    public int getTestQuesBlockType() {
        return testQuesBlockType;
    }

    public void setTestQuesBlockType(int testQuesBlockType) {
        this.testQuesBlockType = testQuesBlockType;
    }

    public String getTestQuesBlockName() {
        return testQuesBlockName;
    }

    public void setTestQuesBlockName(String testQuesBlockName) {
        this.testQuesBlockName = testQuesBlockName;
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

    public String getTestQuesBlockCode() {
        return testQuesBlockCode;
    }

    public void setTestQuesBlockCode(String testQuesBlockCode) {
        this.testQuesBlockCode = testQuesBlockCode;
    }

    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }

    public void setRectangleList(List<Rectangle> rectangleList) {
        this.rectangleList = rectangleList;
    }
}
