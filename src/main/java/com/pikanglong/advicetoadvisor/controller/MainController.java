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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.io.File;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Pi
 * @create 2020-06-25 18:13
 */
@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private AnswerService answerService;

    @PermitAll
    @GetMapping("/")
    public String index(Model model, Map<String, Object> map, @CookieValue(value = "submitted", defaultValue = "notSubmitted") String submitted) {
        File file = new File(".install");
        if (!file.exists()) {
            return "install";
        } else {
            if(!submitted.equals("notSubmitted")) {
                map.put("title", "你已经提交过啦");
                map.put("content", "刷票是不可以的呢~");
                return "info";
            }
            List<String> colleges = userService.getCollegesAll();
            model.addAttribute("colleges", colleges);
            List<ProblemEntity> problems = problemService.getProblems();
            model.addAttribute("problems", problems);
            if(colleges == null || colleges.size() <= 0) {
                model.addAttribute("advisors", null);
                return "index";
            }
            List<AdvisorEntity> advisors = advisorService.getAdvisorsByCollegeString(colleges.get(0));
            model.addAttribute("advisors", advisors);
            return "index";
        }
    }

    @PermitAll
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/admin")
    public String admin(Principal principal, Model model) {
        String authority = userService.getAuthority(principal);
        if (authority.equals("ROLE_ADMIN")) {
            List<UserEntity> collegesEntites = userService.getCollegesEntites();
            model.addAttribute("colleges", collegesEntites);

            List<String> collegeNames = new ArrayList<>();
            List<Integer> collegeCounts = new ArrayList<>();

            for(UserEntity u : collegesEntites) {
                collegeNames.add(u.getCollege());
                collegeCounts.add(u.getCount());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String collegeNamesJsonStr = objectMapper.writeValueAsString(collegeNames);
                String collegeCountsJsonStr = objectMapper.writeValueAsString(collegeCounts);
                model.addAttribute("collegeNames", collegeNamesJsonStr);
                model.addAttribute("collegeCounts", collegeCountsJsonStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            List<ProblemEntity> problems = problemService.getProblems();
            model.addAttribute("problems", problems);

            List<Map<String, Object> > table = new ArrayList<>();
            List<AdvisorEntity> advisors = advisorService.getAdvisorsAll();

            for(AdvisorEntity a : advisors) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("college", a.getCollege());
                detail.put("advisor", a.getAdvisor());

                List<Double> score = new ArrayList<>();
                List<AnswerEntity> answers = answerService.getAnswersByCollegeAndAdvisor(a.getCollege(), a.getAdvisor());
                double totalScore = 0;

                for(ProblemEntity p : problems) {
                    if(p.getType()==1) {
                        double ans = 0;
                        int cnt = 0;
                        List<OptionEntity> options = problemService.getOptionsByProblemIdOrderByScoreDESC(p.getId());

                        for(OptionEntity o : options) {
                            for(AnswerEntity an : answers) {
                                if(an.getProblemId()==p.getId() && an.getContent().equals(String.valueOf(o.getRank()))) {
                                    ans += problemService.getOptionByAnswer(an).getScore();
                                    cnt++;
                                }
                            }
                        }

                        if(cnt != 0) {
                            ans /= cnt;
                        }
                        totalScore += ans;
                        score.add(ans);
                    }
                }

                detail.put("score", score);
                detail.put("totalScore", totalScore);
                table.add(detail);
            }

            table.sort((o1, o2) -> {
                double key1 = (double)o1.get("totalScore");
                double key2 = (double)o2.get("totalScore");
                return key2-key1>0.000001?1:-1;
            });

            int rank = 1;
            for(int i = 0; i < table.size(); i++){
                if(i > 0) {
                    double curr = (double)table.get(i).get("totalScore");
                    double last = (double)table.get(i-1).get("totalScore");
                    if(curr - last > 0.000001 || last - curr > 0.000001) {
                        table.get(i).put("rank", rank);
                    } else {
                        table.get(i).put("rank", table.get(i - 1).get("rank"));
                    }
                } else {
                    table.get(i).put("rank", rank);
                }
                rank++;
            }

            model.addAttribute("table", table);
        } else {
            String college = userService.getUserByUsername(principal.getName()).getCollege();
            List<AdvisorEntity> advisorEntities = advisorService.getAdvisorsByCollegeString(college);
            List<String> advisorNames = new ArrayList<>();
            List<Integer> advisorCounts = new ArrayList<>();

            for(AdvisorEntity a : advisorEntities) {
                advisorNames.add(a.getAdvisor());
                advisorCounts.add(a.getCount());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String collegeNamesJsonStr = objectMapper.writeValueAsString(advisorNames);
                String collegeCountsJsonStr = objectMapper.writeValueAsString(advisorCounts);
                model.addAttribute("collegeNames", collegeNamesJsonStr);
                model.addAttribute("collegeCounts", collegeCountsJsonStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            List<ProblemEntity> problems = problemService.getProblems();
            model.addAttribute("problems", problems);

            List<Map<String, Object> > table = new ArrayList<>();
            List<AdvisorEntity> advisors = advisorService.getAdvisorsByCollegeString(college);

            for(AdvisorEntity a : advisors) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("college", a.getCollege());
                detail.put("advisor", a.getAdvisor());

                List<Double> score = new ArrayList<>();
                List<AnswerEntity> answers = answerService.getAnswersByCollegeAndAdvisor(a.getCollege(), a.getAdvisor());
                double totalScore = 0;

                for(ProblemEntity p : problems) {
                    if(p.getType()==1) {
                        double ans = 0;
                        int cnt = 0;
                        List<OptionEntity> options = problemService.getOptionsByProblemIdOrderByScoreDESC(p.getId());

                        for(OptionEntity o : options) {
                            for(AnswerEntity an : answers) {
                                if(an.getProblemId()==p.getId() && an.getContent().equals(String.valueOf(o.getRank()))) {
                                    ans += problemService.getOptionByAnswer(an).getScore();
                                    cnt++;
                                }
                            }
                        }

                        if(cnt != 0) {
                            ans /= cnt;
                        }
                        totalScore += ans;
                        score.add(ans);
                    }
                }

                detail.put("score", score);
                detail.put("totalScore", totalScore);
                table.add(detail);
            }

            table.sort((o1, o2) -> {
                double key1 = (double)o1.get("totalScore");
                double key2 = (double)o2.get("totalScore");
                return key2-key1>0.000001?1:-1;
            });

            int rank = 1;
            for(int i = 0; i < table.size(); i++){
                if(i > 0) {
                    double curr = (double)table.get(i).get("totalScore");
                    double last = (double)table.get(i-1).get("totalScore");
                    if(curr - last > 0.000001 || last - curr > 0.000001) {
                        table.get(i).put("rank", rank);
                    } else {
                        table.get(i).put("rank", table.get(i - 1).get("rank"));
                    }
                } else {
                    table.get(i).put("rank", rank);
                }
                rank++;
            }

            model.addAttribute("table", table);
        }
        return "admin";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/account")
    public String account(Model model) {
        List<UserEntity> userEntities = userService.getUsersAll();
        model.addAttribute("account", userEntities);
        return "account";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/account/add")
    public String addAccount() {
        return "addaccount";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/account/edit/{username}")
    public String editAccount(@PathVariable("username") String username, Model model) {
        UserEntity userEntity = userService.getUserByUsername(username);
        model.addAttribute("user", userEntity);
        return "addaccount";
    }
}
