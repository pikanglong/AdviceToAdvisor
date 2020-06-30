package com.pikanglong.advicetoadvisor.controller;

import com.pikanglong.advicetoadvisor.entity.AdvisorEntity;
import com.pikanglong.advicetoadvisor.entity.UserEntity;
import com.pikanglong.advicetoadvisor.service.AdvisorService;
import com.pikanglong.advicetoadvisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Pi
 * @create 2020-06-27 19:03
 */
@Controller
public class AdvisorApiController {
    @Autowired
    UserService userService;

    @Autowired
    AdvisorService advisorService;

    @RolesAllowed("ADMIN")
    @PostMapping("/api/advisor/add")
    public String addAdvisor(AdvisorEntity advisorEntity, Map<String, Object> map) {
        // 判断姓名是否为空
        String advisor = advisorEntity.getAdvisor();
        if(advisor == null || advisor.length() == 0) {
            map.put("msg", "姓名为空");
            return "addadvisor";
        }
        // 判断学院是否为空
        String college = advisorEntity.getCollege();
        if(college == null || college.length() == 0){
            map.put("msg", "学院为空");
            return "addadvisor";
        }
        // 判断学院是否存在
        UserEntity collegeUser = userService.getUserByCollege(advisorEntity.getCollege());
        if(collegeUser == null) {
            map.put("msg", "学院不存在");
            return "addadvisor";
        }

        String[] advisors = advisor.split("；");
        List<AdvisorEntity> advisorEntities = new ArrayList<>();
        for(int i = 0; i < advisors.length; i++) {
            advisors[i] = advisors[i].replaceAll(" +"," ");
            AdvisorEntity entity = new AdvisorEntity();
            entity.setAdvisor(advisors[i]);
            entity.setCollege(advisorEntity.getCollege());
            advisorEntities.add(entity);
        }

        advisorService.AddAdvisors(advisorEntities);
        return "redirect:/advisor";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/api/advisor/edit")
    public String editAdvisor(AdvisorEntity advisorEntity, Map<String, Object> map, Model model) {
        AdvisorEntity editAdvisor = advisorService.getAdvisorById(advisorEntity.getId());
        // 判断是不是来搞事的
        if(editAdvisor == null) {
            return "redirect:/advisor";
        }
        // 判断姓名是否为空
        String name = advisorEntity.getAdvisor();
        if(name == null || name.length() == 0) {
            map.put("msg", "姓名为空");
            model.addAttribute("advisor", editAdvisor);
            return "addadvisor";
        }
        // 判断学院是否为空
        String college = advisorEntity.getCollege();
        if(college == null || college.length() == 0) {
            map.put("msg", "学院为空");
            model.addAttribute("advisor", editAdvisor);
            return "addadvisor";
        }
        // 判断学院是否存在
        UserEntity collegeUser = userService.getUserByCollege(advisorEntity.getCollege());
        if(collegeUser == null) {
            map.put("msg", "学院不存在");
            model.addAttribute("advisor", editAdvisor);
            return "addadvisor";
        }
        advisorEntity.setAdvisor(advisorEntity.getAdvisor().replaceAll(" +", " "));
        advisorService.updateAdvisor(advisorEntity);
        return "redirect:/advisor";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/api/advisor/delete/{id}")
    public String deleteAdvisor(@PathVariable("id")int id) {
        advisorService.deleteAdvisor(id);
        return "redirect:/advisor";
    }

    @PermitAll
    @ResponseBody
    @GetMapping("/api/advisor/select")
    public List<AdvisorEntity> selectAdvisor(String college) {
        List<AdvisorEntity> advisorEntities = advisorService.getAdvisorsByCollegeString(college);
        return advisorEntities;
    }
}
