package com.pikanglong.advicetoadvisor.mapper;

import com.pikanglong.advicetoadvisor.entity.AnswerEntity;
import com.pikanglong.advicetoadvisor.entity.OptionEntity;
import com.pikanglong.advicetoadvisor.entity.ProblemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-28 16:41
 */
@Mapper
public interface ProblemMapper {
    List<OptionEntity> selectOptionsByProblemId(int problemId);

    List<ProblemEntity> selectProblems();

    void deleteProblems();

    void deleteOptions();

    void insertProblem(ProblemEntity problemEntity);

    void insertOptions(List<OptionEntity> optionEntities);

    List<OptionEntity> selectOptionsByProblemIdOrderByScoreDESC(int problemId);

    OptionEntity selectOptionByAnswer(AnswerEntity answerEntity);

    List<ProblemEntity> selectProblemsByType(int type);

    ProblemEntity selectProblemById(int problemId);

}
