package com.pikanglong.advicetoadvisor.service.impl;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;
import com.pikanglong.advicetoadvisor.entity.UserEntity;
import com.pikanglong.advicetoadvisor.mapper.AdvisorMapper;
import com.pikanglong.advicetoadvisor.mapper.AnswerMapper;
import com.pikanglong.advicetoadvisor.mapper.UserMapper;
import com.pikanglong.advicetoadvisor.service.AdvisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-27 19:24
 */
@Service
public class AdvisorServiceImpl implements AdvisorService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AdvisorMapper advisorMapper;

    @Autowired
    AnswerMapper answerMapper;

    @Override
    public void AddAdvisors(List<AdvisorEntity> advisorEntities) {
        advisorMapper.insertAdvisors(advisorEntities);
    }

    @Override
    public List<AdvisorEntity> getAdvisorsAll() {
        List<AdvisorEntity> advisorEntities = advisorMapper.selectAdvisors();
        return advisorEntities;
    }

    @Override
    public List<AdvisorEntity> getAdvisorsByCollege(Principal principal) {
        String username = principal.getName();
        UserEntity userEntity = userMapper.selectUserByUsername(username);
        List<AdvisorEntity> advisorEntities = advisorMapper.selectAdvisorsByCollege(userEntity.getCollege());
        return advisorEntities;
    }

    @Override
    public List<AdvisorEntity> getAdvisorsByCollegeString(String college) {
        List<AdvisorEntity> advisorEntities = advisorMapper.selectAdvisorsByCollege(college);
        return advisorEntities;
    }

    @Override
    public AdvisorEntity getAdvisorById(int id) {
        AdvisorEntity advisorEntity = advisorMapper.selectAdcisorById(id);
        return advisorEntity;
    }

    @Override
    public void updateAdvisor(AdvisorEntity advisorEntity) {
        advisorMapper.updateAdvisor(advisorEntity);
    }

    @Override
    public void deleteAdvisor(int id) {
        answerMapper.deleteAnswersByAdvisorId(id);
        advisorMapper.deleteAdvisor(id);
    }

    @Override
    public AdvisorEntity getAdvisorByCollegeAndAdvisor(String college, String advisor) {
        AdvisorEntity advisorEntity = advisorMapper.selectAdvisorByCollegeAndAdvisor(college, advisor);
        return advisorEntity;
    }

    @Override
    @Transactional
    public void increaseAdvisorCount(int id) {
        advisorMapper.increaseAdvisorCount(id);
    }
}
