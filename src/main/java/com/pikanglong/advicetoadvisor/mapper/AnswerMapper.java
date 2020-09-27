package com.pikanglong.advicetoadvisor.mapper;

import com.pikanglong.advicetoadvisor.entity.AnswerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-29 19:07
 */
@Mapper
public interface AnswerMapper {
    List<AnswerEntity> getAnswersByCsrf(String csrf);

    void insertAnswers(List<AnswerEntity> answerEntities);

    List<AnswerEntity> getAnswersByAdvisorId(int id);

    void deleteAnswers();

    void deleteAnswersByAdvisorId(int id);

    List<AnswerEntity> getAnswersByProblemId(int problem);
}
