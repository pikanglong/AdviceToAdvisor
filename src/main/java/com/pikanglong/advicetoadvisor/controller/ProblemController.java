package com.pikanglong.advicetoadvisor.controller;

import com.pikanglong.advicetoadvisor.entity.ProblemEntity;
import com.pikanglong.advicetoadvisor.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * @author Carlos Pi
 * @create 2020-06-27 21:17
 */
@Controller
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @RolesAllowed("ADMIN")
    @GetMapping("/design")
    public String design(Model model) {
        List<ProblemEntity> problemEntities = problemService.getProblems();
        model.addAttribute("problems", problemEntities);
        return "design";
    }
}
