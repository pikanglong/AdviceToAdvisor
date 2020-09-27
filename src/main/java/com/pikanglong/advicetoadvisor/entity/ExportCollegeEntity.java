package com.pikanglong.advicetoadvisor.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * @Author whz
 * @Date 2020/9/27 10:19
 **/
public class ExportCollegeEntity extends BaseRowModel {

    @ExcelProperty(value = "学院", index = 0)
    private String college;
    @ExcelProperty(value = "已完成人数", index = 1)
    private int count;

    public int getCount() {
        return count;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
