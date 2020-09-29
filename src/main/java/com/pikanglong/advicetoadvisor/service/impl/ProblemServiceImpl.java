package com.pikanglong.advicetoadvisor.service.impl;

import com.pikanglong.advicetoadvisor.entity.AnswerEntity;
import com.pikanglong.advicetoadvisor.entity.ExportCollegeEntity;
import com.pikanglong.advicetoadvisor.entity.OptionEntity;
import com.pikanglong.advicetoadvisor.entity.ProblemEntity;
import com.pikanglong.advicetoadvisor.mapper.AnswerMapper;
import com.pikanglong.advicetoadvisor.mapper.ProblemMapper;
import com.pikanglong.advicetoadvisor.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-28 17:31
 */
@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public List<ProblemEntity> getProblems() {
        List<ProblemEntity> problemEntities = problemMapper.selectProblems();
        return problemEntities;
    }

    @Override
    public void deleteProblems() {
        answerMapper.deleteAnswers();
        problemMapper.deleteOptions();
        problemMapper.deleteProblems();
    }

    @Override
    public int insertProblem(ProblemEntity problemEntity) {
        problemMapper.insertProblem(problemEntity);
        return problemEntity.getId();
    }

    @Override
    public void insertOptions(List<OptionEntity> optionEntities) {
        problemMapper.insertOptions(optionEntities);
    }

    @Override
    public List<OptionEntity> getOptionsByProblemIdOrderByScoreDESC(int id) {
        List<OptionEntity> optionEntities = problemMapper.selectOptionsByProblemIdOrderByScoreDESC(id);
        return optionEntities;
    }

    @Override
    public OptionEntity getOptionByAnswer(AnswerEntity answerEntity) {
        OptionEntity optionEntity = problemMapper.selectOptionByAnswer(answerEntity);
        return optionEntity;
    }

    @Override
    public List<ProblemEntity> getProblemEntity(int type) {
        List<ProblemEntity> problemList = problemMapper.selectProblemsByType(type);
        return problemList;
    }

    @Override
    public ProblemEntity getProblemById(int problemId) {
        ProblemEntity problemEntitiy = problemMapper.selectProblemById(problemId);
        return problemEntitiy;
    }
}
