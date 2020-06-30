package com.pikanglong.advicetoadvisor.controller;

import com.pikanglong.advicetoadvisor.entity.OptionEntity;
import com.pikanglong.advicetoadvisor.entity.ProblemEntity;
import com.pikanglong.advicetoadvisor.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-28 21:44
 */
@Controller
public class ProblemApiController {
    @Autowired
    ProblemService problemService;

    @RolesAllowed("ADMIN")
    @PostMapping("/api/design")
    public String design(String[] type, String[] title, String[] options, int[] score) {
        /*
         * 此表单如果不符合审查要求
         * 一定是自作孽不可活
         * 考虑到只有admin用户有此接口的权限
         * admin应该也不会折腾API
         * 所以检查出问题直接重定向回去
         */
        int count = 0;
        // 检查类型是否有误
        for(String t : type) {
            if(t.equals("单选")) {
                count++;
            }
            if(!t.equals("单选") && !t.equals("问答")) {
                return "redirect:/design";
            }
        }
        // 检查数量能不能对的上
        if(type.length != title.length || count*5 != options.length || count*5 != score.length) {
            return "redirect:/design";
        }

        int cnt = 0;
        problemService.deleteProblems();
        for(int rank = 0; rank < type.length; rank++) {
            String thisTitle = title[rank];
            if(type[rank].equals("单选")) {
                ProblemEntity problemEntity = new ProblemEntity();
                problemEntity.setRank(rank+1);
                problemEntity.setContent(thisTitle);
                problemEntity.setType(1);
                int problemId = problemService.insertProblem(problemEntity);

                List<OptionEntity> optionEntities = new ArrayList<>();
                for(int i = 0; i < 5; i++) {
                    OptionEntity optionEntity = new OptionEntity();
                    optionEntity.setProblemId(problemId);
                    optionEntity.setRank(i+1);
                    optionEntity.setContent(options[5*cnt+i]);
                    optionEntity.setScore(score[5*cnt+i]);
                    optionEntities.add(optionEntity);
                }
                problemService.insertOptions(optionEntities);
                cnt++;
            } else {
                ProblemEntity problemEntity = new ProblemEntity();
                problemEntity.setRank(rank+1);
                problemEntity.setContent(thisTitle);
                problemEntity.setType(2);
                problemService.insertProblem(problemEntity);
            }
        }
        return "redirect:/design";
    }
}
