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
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Pi
 * @create 2020-06-25 20:30
 */
@Controller
public class MainApiController {
    @Autowired
    private UserService userService;

    @Autowired
    ProblemService problemService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AdvisorService advisorService;

    @PermitAll
    @ResponseBody
    @PostMapping("/api/install")
    public String install(String password) {
        File file = new File(".install");
        if (!file.exists()) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername("admin");
            userEntity.setPassword(password);
            userService.addAdmin(userEntity);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/api/account/add")
    public String addAccount(UserEntity userEntity, Map<String, Object> map) {
        // 判断用户名是否为空
        String username = userEntity.getUsername();
        if(username == null || username.length() == 0) {
            map.put("msg", "用户名为空");
            return "addaccount";
        }
        // 判断密码是否为空
        String password = userEntity.getPassword();
        if(password == null || password.length() == 0) {
            map.put("msg", "密码为空");
            return "addaccount";
        }
        // 判断学院是否为空
        String college = userEntity.getCollege();
        if(college == null || college.length() == 0) {
            map.put("msg", "学院为空");
            return "addaccount";
        }
        // 判断用户名是否存在
        UserEntity usernameUser = userService.getUserByUsernameFromAll(username);
        if(usernameUser != null) {
            map.put("msg", "用户名已存在或曾被使用过");
            return "addaccount";
        }
        // 判断学院是否存在
        UserEntity collegeUser = userService.getUserByCollege(college);
        if(collegeUser != null) {
            map.put("msg", "学院已存在");
            return "addaccount";
        }
        userService.addUser(userEntity);
        return "redirect:/account";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/api/account/edit")
    public String editAccount(UserEntity userEntity, Map<String, Object> map, Model model) {
        String username = userEntity.getUsername();
        UserEntity editUser = userService.getUserByUsername(username);
        // 判断是不是来搞事的
        if(editUser == null || username == null || username.length() == 0) {
            return "redirect:/account";
        }
        // 判断修改的是否为管理员
        if(username.equals("admin")) {
            return "redirect:/account";
        }
        // 判断密码是否为空
        String password = userEntity.getPassword();
        if(password == null || password.length() == 0) {
            userEntity.setPassword(editUser.getPassword());
        }
        // 判断学院是否为空
        String college = userEntity.getCollege();
        if(college == null || college.length() == 0) {
            map.put("msg", "学院为空");
            model.addAttribute("user", editUser);
            return "addaccount";
        }
        // 判断学院是否存在
        UserEntity collegeUser = userService.getUserByCollege(college);
        if(collegeUser != null && !collegeUser.getUsername().equals(username)) {
            map.put("msg", "学院已存在");
            model.addAttribute("user", editUser);
            return "addaccount";
        }
        userService.updateUser(userEntity);
        return "redirect:/account";
    }

    @RolesAllowed("ADMIN")
    @PostMapping("/api/account/delete/{username}")
    public String deleteAccount(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return "redirect:/account";
    }

    @PermitAll
    @PostMapping("/submit")
    public String submit(HttpServletResponse response,
                         String college, String advisor,
                         AnswerModel answers, String _csrf,
                         Map<String, Object> map,
                         @CookieValue(value = "submitted", defaultValue = "notSubmitted") String submitted) {
        List<AnswerEntity> csrfCheck = answerService.getAnswersByCsrf(_csrf);
        if(!(csrfCheck == null || csrfCheck.size() <= 0)) {
            map.put("title", "你已经提交过啦");
            map.put("content", "刷票是不可以的呢~");
            return "info";
        }
        if(!submitted.equals("notSubmitted")) {
            map.put("title", "你已经提交过啦");
            map.put("content", "刷票是不可以的呢~");
            return "info";
        }
        List<AnswerEntity> answerEntities = answers.getAnswers();
        if(answerEntities == null || answerEntities.size() <= 0) {
            map.put("title", "错误");
            map.put("content", "问题列表为空");
            return "info";
        }

        int advisorId = advisorService.getAdvisorByCollegeAndAdvisor(college, advisor).getId();
        for(AnswerEntity a : answerEntities) {
            a.setAdvisorId(advisorId);
            a.set_csrf(_csrf);
        }
        answerService.insertAnswers(answerEntities);
        userService.increaseCollegeCount(college);
        advisorService.increaseAdvisorCount(advisorId);

        Cookie cookie = new Cookie("submitted", "submitted");
        cookie.setMaxAge(30 * 24 * 60 * 60);
        response.addCookie(cookie);

        map.put("title", "提交成功");
        map.put("content", "感谢您参与本次调查！");
        return "info";
    }

    @RolesAllowed("ADMIN")
    @ResponseBody
    @GetMapping("/api/changechart")
    public Map<String, Object> changeChart(String collegeName) {
        Map<String, Object> map = new HashMap<>();
        if(collegeName.equals("所有学院")) {
            List<UserEntity> colleges = userService.getCollegesEntites();
            List<String> collegeNames = new ArrayList<>();
            List<Integer> collegeCounts = new ArrayList<>();
            for(UserEntity u : colleges) {
                collegeNames.add(u.getCollege());
                collegeCounts.add(u.getCount());
            }
            map.put("labels", collegeNames);
            map.put("data", collegeCounts);
        } else {
            List<AdvisorEntity> advisors = advisorService.getAdvisorsByCollegeString(collegeName);
            List<String> advisorNames = new ArrayList<>();
            List<Integer> advisorCounts = new ArrayList<>();
            for(AdvisorEntity a : advisors) {
                advisorNames.add(a.getAdvisor());
                advisorCounts.add(a.getCount());
            }
            map.put("labels", advisorNames);
            map.put("data", advisorCounts);
        }
        return map;
    }

    @RolesAllowed("ADMIN")
    @ResponseBody
    @GetMapping("/api/changetable")
    public List<Map<String, Object> > changeTable(String collegeName) {
        List<ProblemEntity> problems = problemService.getProblems();
        List<Map<String, Object> > table = new ArrayList<Map<String, Object> >();
        if(collegeName.equals("所有学院")) {
            List<AdvisorEntity> advisors = advisorService.getAdvisorsAll();

            for(AdvisorEntity a : advisors) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("college", a.getCollege());
                detail.put("advisor", a.getAdvisor());

                List<Double> score = new ArrayList<>();
                List<AnswerEntity> answers = answerService.getAnswersByCollegeAndAdvisor(a.getCollege(), a.getAdvisor());
                double totalScore = 0;

                for (ProblemEntity p : problems) {
                    if (p.getType() == 1) {
                        double ans = 0;
                        int cnt = 0;
                        List<OptionEntity> options = problemService.getOptionsByProblemIdOrderByScoreDESC(p.getId());

                        for (OptionEntity o : options) {
                            for (AnswerEntity an : answers) {
                                if (an.getProblemId() == p.getId() && an.getContent().equals(String.valueOf(o.getRank()))) {
                                    ans += problemService.getOptionByAnswer(an).getScore();
                                    cnt++;
                                }
                            }
                        }

                        if (cnt != 0) {
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
    } else {
            List<AdvisorEntity> advisors = advisorService.getAdvisorsByCollegeString(collegeName);

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
        }
        return table;
    }
}
