package com.pikanglong.advicetoadvisor.service.impl;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.pikanglong.advicetoadvisor.service.ExcelService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * @Author whz
 * @Date 2020/9/27 10:19
 **/
@Service
public class ExcelServiceImpl implements ExcelService {


    /**
     * description:
     *
     * @param os
     * @param clazz
     * @param data
     * @param sheetNo
     * @return java.lang.Boolean
     */
    @Override
    public Boolean writeExcel(OutputStream os, Class clazz, List<? extends BaseRowModel> data, int sheetNo) {

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(os);
            ExcelWriter writer = new ExcelWriter(bos, ExcelTypeEnum.XLSX);
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(sheetNo, 1, clazz);
            writer.write(data, sheet1);
            writer.finish();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
