package com.pikanglong.advicetoadvisor.service;

import com.pikanglong.advicetoadvisor.entity.AnswerEntity;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-29 20:32
 */
public interface AnswerService {
    List<AnswerEntity> getAnswersByCsrf(String csrf);

    void insertAnswers(List<AnswerEntity> answerEntities);

    List<AnswerEntity> getAnswersByCollegeAndAdvisor(String college, String advisor);
}
