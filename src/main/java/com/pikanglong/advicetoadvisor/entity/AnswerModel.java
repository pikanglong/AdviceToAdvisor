package com.pikanglong.advicetoadvisor.entity;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-29 18:59
 */
public class AnswerModel {
    private List<AnswerEntity> answers;

    public AnswerModel() {
        super();
    }

    public AnswerModel(List<AnswerEntity> answers) {
        super();
        this.answers = answers;
    }

    public List<AnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerEntity> answers) {
        this.answers = answers;
    }
}
