/**
 *
 */
package com.service.eurekaclient.model;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
public class PaperTemplate {
    /**
     * @ClassName:PaperTemplate
     * @Description:試卷模板
     */
    // 模板id
    private String id;

    private String examPaperId;

    private String templateOrder;

    private String templateName;

    private String description;

    private int templateType;

    private String templateImg;

    private String status;

    private String createTime;

    private String modifyTime;

    List<TestQuesBlock> testQuesBlockList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(String examPaperId) {
        this.examPaperId = examPaperId;
    }

    public String getTemplateOrder() {
        return templateOrder;
    }

    public void setTemplateOrder(String templateOrder) {
        this.templateOrder = templateOrder;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemplateType() {
        return templateType;
    }

    public void setTemplateType(int templateType) {
        this.templateType = templateType;
    }

    public String getTemplateImg() {
        return templateImg;
    }

    public void setTemplateImg(String templateImg) {
        this.templateImg = templateImg;
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

    public List<TestQuesBlock> getTestQuesBlockList() {
        return testQuesBlockList;
    }

    public void setTestQuesBlockList(List<TestQuesBlock> testQuesBlockList) {
        this.testQuesBlockList = testQuesBlockList;
    }
}
