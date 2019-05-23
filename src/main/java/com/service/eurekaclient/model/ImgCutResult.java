package com.service.eurekaclient.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class ImgCutResult {
    /**
     * @ClassName:ImgCutResult
     * @Description:试卷切割 结果
     */
    // 0：结果正常 0>：切割異常
    private int cutResult;

    private BigDecimal examId;

    private BigDecimal roomId;

    public BigDecimal getExamId() {
        return examId;
    }

    public void setExamId(BigDecimal examId) {
        this.examId = examId;
    }

    public BigDecimal getRoomId() {
        return roomId;
    }

    public void setRoomId(BigDecimal roomId) {
        this.roomId = roomId;
    }

    // 描述
    private String desc;
    // 考生id
    private String userId;
    // 试卷id
    private String answerPaperId;
    //试卷模板id
    private String paperTemplateId;
    // 切割结果
    private List<Coordinate> list = new ArrayList<Coordinate>();

    public int getCutResult() {
        return cutResult;
    }

    public void setCutResult(int cutResult) {
        this.cutResult = cutResult;
    }

    public String getPaperTemplateId() {
        return paperTemplateId;
    }

    public void setPaperTemplateId(String paperTemplateId) {
        this.paperTemplateId = paperTemplateId;
    }

    public String getDesc() {
        return desc;

    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswerPaperId() {
        return answerPaperId;
    }

    public void setAnswerPaperId(String answerPaperId) {
        this.answerPaperId = answerPaperId;
    }

    public List<Coordinate> getList() {
        return list;
    }

    public void setList(List<Coordinate> list) {
        this.list = list;
    }
}
