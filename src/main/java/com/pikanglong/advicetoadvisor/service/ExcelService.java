package com.pikanglong.advicetoadvisor.service;

import com.alibaba.excel.metadata.BaseRowModel;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author whz
 * @Date 2020/9/27 10:19
 **/
public interface ExcelService {
    Boolean writeExcel(OutputStream os, Class clazz, List<? extends BaseRowModel> data, int sheetNo);
}
