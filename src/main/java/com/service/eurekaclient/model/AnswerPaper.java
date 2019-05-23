/**
 *
 */
package com.service.eurekaclient.model;

import lombok.Data;

/**
 * @author Administrator
 */
public class AnswerPaper {
    /**
     * @ClassName:AQnswerPaper
     * @Description:
     */

    private String paperId;

    private String answerPaperId;

    private String paperTemplateId;
    // 上传路径
    private String uploadPath;
    // 上传图片名称
    private String uploadFileName;

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    public String getAnswerPaperId() {
        return answerPaperId;
    }

    public void setAnswerPaperId(String answerPaperId) {
        this.answerPaperId = answerPaperId;
    }

    public String getPaperTemplateId() {
        return paperTemplateId;
    }

    public void setPaperTemplateId(String paperTemplateId) {
        this.paperTemplateId = paperTemplateId;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }
}
