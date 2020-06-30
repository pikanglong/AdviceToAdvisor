package com.pikanglong.advicetoadvisor.service;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;

import java.security.Principal;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-27 19:22
 */
public interface AdvisorService {
    void AddAdvisors(List<AdvisorEntity> advisorEntities);

    List<AdvisorEntity> getAdvisorsAll();

    List<AdvisorEntity> getAdvisorsByCollege(Principal principal);

    List<AdvisorEntity> getAdvisorsByCollegeString(String college);

    AdvisorEntity getAdvisorById(int id);

    void updateAdvisor(AdvisorEntity advisorEntity);

    void deleteAdvisor(int id);

    AdvisorEntity getAdvisorByCollegeAndAdvisor(String college, String advisor);

    void increaseAdvisorCount(int id);
}
