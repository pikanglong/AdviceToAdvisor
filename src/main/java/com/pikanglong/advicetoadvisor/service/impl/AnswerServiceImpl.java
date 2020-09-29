package com.pikanglong.advicetoadvisor.service.impl;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;
import com.pikanglong.advicetoadvisor.entity.AnswerEntity;
import com.pikanglong.advicetoadvisor.mapper.AdvisorMapper;
import com.pikanglong.advicetoadvisor.mapper.AnswerMapper;
import com.pikanglong.advicetoadvisor.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-29 20:33
 */
@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AdvisorMapper advisorMapper;

    @Override
    public List<AnswerEntity> getAnswersByCsrf(String csrf) {
        List<AnswerEntity> answerEntities = answerMapper.getAnswersByCsrf(csrf);
        return answerEntities;
    }

    @Override
    public void insertAnswers(List<AnswerEntity> answerEntities) {
        answerMapper.insertAnswers(answerEntities);
    }

    @Override
    public List<AnswerEntity> getAnswersByCollegeAndAdvisor(String college, String advisor) {
        AdvisorEntity advisorEntity = advisorMapper.selectAdvisorByCollegeAndAdvisor(college, advisor);
        List<AnswerEntity> answerEntities = answerMapper.getAnswersByAdvisorId(advisorEntity.getId());
        return answerEntities;
    }

    @Override
    public List<AnswerEntity> getAnswersByProblemId(int problem) {
        List<AnswerEntity> answerEntities = answerMapper.getAnswersByProblemId(problem);
        return answerEntities;
    }
}
