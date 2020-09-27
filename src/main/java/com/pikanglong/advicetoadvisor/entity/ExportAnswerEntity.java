package com.pikanglong.advicetoadvisor.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @Author whz
 * @Date 2020/9/27 13:39
 **/
public class ExportAnswerEntity extends BaseRowModel {
    @ExcelProperty(value = "问题", index = 0)
    private String problem;
    @ExcelProperty(value = "辅导员", index = 1)
    private String advisor;
    @ExcelProperty(value = "回答", index = 2)
    private String answer;

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getAdvisor() {
        return advisor;
    }

    public String getAnswer() {
        return answer;
    }

    public String getProblem() {
        return problem;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}

