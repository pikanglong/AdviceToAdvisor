package com.pikanglong.advicetoadvisor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pikanglong.advicetoadvisor.entity.*;
import com.pikanglong.advicetoadvisor.service.AdvisorService;
import com.pikanglong.advicetoadvisor.service.AnswerService;
import com.pikanglong.advicetoadvisor.service.ProblemService;
import com.pikanglong.advicetoadvisor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Pi
 * @create 2020-06-27 17:00
 */
@Controller
public class AdvisorController {
    @Autowired
    UserService userService;

    @Autowired
    AdvisorService advisorService;

    @Autowired
    ProblemService problemService;

    @Autowired
    AnswerService answerService;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/advisor")
    public String advisor(Principal principal, Model model) {
        String authority = userService.getAuthority(principal);
        if(authority.equals("ROLE_ADMIN")) {
            List<AdvisorEntity> advisorEntities = advisorService.getAdvisorsAll();
            model.addAttribute("advisors", advisorEntities);
        } else {
            List<AdvisorEntity> advisorEntities = advisorService.getAdvisorsByCollege(principal);
            model.addAttribute("advisors", advisorEntities);
        }
        return "advisor";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/advisor/add")
    public String addAdvisor(Model model) {
        List<String> colleges = userService.getCollegesAll();
        model.addAttribute("colleges", colleges);
        return "addadvisor";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/advisor/edit/{id}")
    public String editAdvisor(@PathVariable("id") int id, Model model) {
        AdvisorEntity advisorEntity = advisorService.getAdvisorById(id);
        model.addAttribute("advisor", advisorEntity);
        List<String> colleges = userService.getCollegesAll();
        model.addAttribute("colleges", colleges);
        return "addadvisor";
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/advisor/detail/{id}")
    public String detail(Principal principal, @PathVariable("id") int id, Model model) {
        String authority = userService.getAuthority(principal);

        if(authority.equals("ROLE_USER")) {
            UserEntity user = userService.getUserByUsername(principal.getName());
            String college = user.getCollege();
            AdvisorEntity advisorEntity = advisorService.getAdvisorById(id);
            if(!college.equals(advisorEntity.getCollege())) {
                return "redirect:/advisor";
            }
        }

        List<ProblemEntity> problems = problemService.getProblems();
        model.addAttribute("problems", problems);

        AdvisorEntity advisor = advisorService.getAdvisorById(id);
        model.addAttribute("advisor", advisor);

        List<AnswerEntity> answers = answerService.getAnswersByCollegeAndAdvisor(advisor.getCollege(), advisor.getAdvisor());

        Map<Integer, Object> optionsMap = new HashMap<>();
        Map<Integer, Object> labels = new HashMap<>();
        Map<Integer, Object> data = new HashMap<>();
        Map<Integer, Object> score = new HashMap<>();
        Map<Integer, Object> comments = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        double totalScore = 0;

        for(ProblemEntity p : problems) {
            if(p.getType()==1){
                double ans = 0;
                int cnt = 0;
                List<OptionEntity> optionsList = new ArrayList<>();
                List<String> optionName = new ArrayList<>();
                List<Integer> optionCount = new ArrayList<>();
                List<OptionEntity> options = problemService.getOptionsByProblemIdOrderByScoreDESC(p.getId());

                for(OptionEntity o : options) {
                    optionsList.add(o);
                    optionName.add(o.getContent());
                    int count = 0;
                    for(AnswerEntity a : answers) {
                        if(a.getProblemId()==p.getId() && a.getContent().equals(String.valueOf(o.getRank()))) {
                            count++;
                            ans += problemService.getOptionByAnswer(a).getScore();
                            cnt++;
                        }
                    }
                    optionCount.add(count);
                }

                try {
                    String optionNameJsonStr = objectMapper.writeValueAsString(optionName);
                    String optionCountJsonStr = objectMapper.writeValueAsString(optionCount);
                    labels.put(p.getId(),optionNameJsonStr);
                    data.put(p.getId(),optionCountJsonStr);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                optionsMap.put(p.getId(), optionsList);
                if(cnt != 0) {
                    ans /= cnt;
                }
                totalScore += ans;
                score.put(p.getId(), ans);
            } else {
                List<String> comment = new ArrayList<>();
                for(AnswerEntity a : answers) {
                    if(a.getProblemId()==p.getId()) {
                        comment.add(a.getContent());
                    }
                }
                comments.put(p.getId(),comment);
            }
        }

        model.addAttribute("options", optionsMap);
        model.addAttribute("labels", labels);
        model.addAttribute("data", data);
        model.addAttribute("score", score);
        model.addAttribute("totalScore", totalScore);
        model.addAttribute("comments", comments);

        return "detail";
    }
}
