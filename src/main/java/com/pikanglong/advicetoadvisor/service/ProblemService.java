package com.pikanglong.advicetoadvisor.service;

import com.pikanglong.advicetoadvisor.entity.AnswerEntity;
import com.pikanglong.advicetoadvisor.entity.OptionEntity;
import com.pikanglong.advicetoadvisor.entity.ProblemEntity;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-28 17:29
 */
public interface ProblemService {
    List<ProblemEntity> getProblems();

    void deleteProblems();

    int insertProblem(ProblemEntity problemEntity);

    void insertOptions(List<OptionEntity> optionEntities);

    List<OptionEntity> getOptionsByProblemIdOrderByScoreDESC(int id);

    OptionEntity getOptionByAnswer(AnswerEntity answerEntity);

    List<ProblemEntity> getProblemEntity(int type);

    ProblemEntity getProblemById(int problemId);

}
