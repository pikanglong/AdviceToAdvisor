package com.pikanglong.advicetoadvisor.mapper;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-27 19:05
 */
@Mapper
public interface AdvisorMapper {
    void insertAdvisors(List<AdvisorEntity> advisorEntities);

    List<AdvisorEntity> selectAdvisors();

    List<AdvisorEntity> selectAdvisorsByCollege(String college);

    AdvisorEntity selectAdcisorById(int id);

    void updateAdvisor(AdvisorEntity advisorEntity);

    void deleteAdvisor(int id);

    AdvisorEntity selectAdvisorByCollegeAndAdvisor(String college, String advisor);

    void increaseAdvisorCount(int id);
}
