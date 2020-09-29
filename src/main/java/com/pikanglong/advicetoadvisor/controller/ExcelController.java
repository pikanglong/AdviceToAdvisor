package com.pikanglong.advicetoadvisor.controller;

import com.alibaba.fastjson.JSON;
import com.pikanglong.advicetoadvisor.entity.*;
import com.pikanglong.advicetoadvisor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author whz
 * @Date 2020/9/27 10:19
 **/
@Controller
@RequestMapping("export")
public class ExcelController {
    @Autowired
    private ProblemService problemService;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ExcelService excelService;

    private final int TYPE_EVALUATE = 2;

    //导出辅导员成绩详情
    @GetMapping("/advisorScore")
    @ResponseBody
    public void exportAdvisorScore(HttpServletResponse response, Principal principal) throws IOException {
        String authority = userService.getAuthority(principal);
        if ("ROLE_ADMIN".equals(authority)) {
            List<ProblemEntity> problems = problemService.getProblems();

            List<Map<String, Object>> table = new ArrayList<>();
            List<AdvisorEntity> advisors = advisorService.getAdvisorsAll();

            for (AdvisorEntity a : advisors) {
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
                double key1 = (double) o1.get("totalScore");
                double key2 = (double) o2.get("totalScore");
                return key2 - key1 > 0.000001 ? 1 : -1;
            });

            int rank = 1;
            for (int i = 0; i < table.size(); i++) {
                if (i > 0) {
                    double curr = (double) table.get(i).get("totalScore");
                    double last = (double) table.get(i - 1).get("totalScore");
                    if (curr - last > 0.000001 || last - curr > 0.000001) {
                        table.get(i).put("rank", rank);
                    } else {
                        table.get(i).put("rank", table.get(i - 1).get("rank"));
                    }
                } else {
                    table.get(i).put("rank", rank);
                }
                rank++;


            }
            List<ExportAdvisorEntity> advisorList = new ArrayList<>();
            for (Map<String, Object> m : table) {
                ExportAdvisorEntity exportAdvisorEntity = JSON.parseObject(JSON.toJSONString(m), ExportAdvisorEntity.class);
                advisorList.add(exportAdvisorEntity);
            }
            String fileName = "辅导员成绩详情.xlsx";
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType("application/octet-stream");
            OutputStream out = response.getOutputStream();
            excelService.writeExcel(out, ExportAdvisorEntity.class, advisorList, 1);
            out.flush();

        }
    }

    //导出各学院完成人数
    @GetMapping("/collegeCount")
    @ResponseBody
    public void collegeCount(HttpServletResponse response, Principal principal) throws IOException {
        String authority = userService.getAuthority(principal);
        if ("ROLE_ADMIN".equals(authority)) {
            List<UserEntity> collegesList = userService.getCollegesEntites();
            List<ExportCollegeEntity> exportCollegeList = new ArrayList<>();
            for (UserEntity u : collegesList) {
                ExportCollegeEntity e = JSON.parseObject(JSON.toJSONString(u), ExportCollegeEntity.class);
                exportCollegeList.add(e);
            }
            String fileName = "各学院完成人数.xlsx";
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType("application/octet-stream");
            OutputStream out = response.getOutputStream();
            excelService.writeExcel(out, ExportCollegeEntity.class, exportCollegeList, 1);
            out.flush();
        }
    }

    //导出问答问题回答
    @GetMapping("/advisorEvaluate")
    @ResponseBody
    public void advisorEvaluate(HttpServletResponse response, Principal principal) throws IOException {
        String authority = userService.getAuthority(principal);
        if ("ROLE_ADMIN".equals(authority)) {
            List<ProblemEntity> problemList = problemService.getProblemEntity(TYPE_EVALUATE);
            List<List<AnswerEntity>> lists = new ArrayList<>();
            List<ExportAnswerEntity> exportAnswerList = new ArrayList<>();
            for (ProblemEntity p : problemList) {
                List<AnswerEntity> answerList = answerService.getAnswersByProblemId(p.getId());
                lists.add(answerList);
            }
            for (List<AnswerEntity> l : lists) {
                for (AnswerEntity a : l) {
                    ExportAnswerEntity e = new ExportAnswerEntity();
                    e.setAnswer(a.getContent());
                    e.setProblem(problemService.getProblemById(a.getProblemId()).getContent());
                    e.setAdvisor(advisorService.getAdvisorById(a.getAdvisorId()).getAdvisor());
                    exportAnswerList.add(e);
                }
            }
            String fileName = "问答问题回答.xlsx";
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.setContentType("application/octet-stream");
            OutputStream out = response.getOutputStream();
            excelService.writeExcel(out, ExportAnswerEntity.class, exportAnswerList, 1);
            out.flush();
        }
    }

    @GetMapping("/authority")
    @ResponseBody
    public Map<String, String> authority(Principal principal) {
        Map<String, String> map = new HashMap<>();
        String authority = userService.getAuthority(principal);
        if ("ROLE_ADMIN".equals(authority)) {
            map.put("msg", "导出成功");
        } else {
            map.put("msg", "导出失败,请联系管理员");
        }
        return map;
    }

}