package com.pikanglong.advicetoadvisor.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @Author whz
 * @Date 2020/9/27 10:19
 **/
public class ExportAdvisorEntity extends BaseRowModel {

    @ExcelProperty(value = "排名", index = 0)
    private String rank;
    @ExcelProperty(value = "学院", index = 1)
    private String college;
    @ExcelProperty(value = "辅导员", index = 2)
    private String advisor;
    @ExcelProperty(value = "总分", index = 3)
    private int totalScore;

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getAdvisor() {
        return advisor;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public String getCollege() {
        return college;
    }

    public String getRank() {
        return rank;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
