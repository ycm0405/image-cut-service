/**
 *
 */
package com.service.eurekaclient.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 */
public class ImgCutInfo {
    /**
     * @ClassName:ImageCutInfo
     * @Description:传入的试卷信息
     */
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

    private PaperTemplate paperTemplateList;

    private List<AnswerPaper> answerPaperList;

    public PaperTemplate getPaperTemplateList() {
        return paperTemplateList;
    }

    public void setPaperTemplateList(PaperTemplate paperTemplateList) {
        this.paperTemplateList = paperTemplateList;
    }

    public List<AnswerPaper> getAnswerPaperList() {
        return answerPaperList;
    }

    public void setAnswerPaperList(List<AnswerPaper> answerPaperList) {
        this.answerPaperList = answerPaperList;
    }
}
