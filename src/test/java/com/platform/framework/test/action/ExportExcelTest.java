/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.action;

import com.google.common.collect.Lists;
import com.platform.framework.util.excel.ExportExcel;
import com.platform.framework.util.excel.ImportExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * 描述：
 * 类名：ExportExcelTest
 * 作者：lufengc
 * 日期：2015/12/2 11:53
 */
public class ExportExcelTest {

    private static Logger log = LoggerFactory.getLogger(ExportExcelTest.class);

    @Test
    public void export(){
        List<String> headerList = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            headerList.add("表头"+i);
        }

        List<String> dataRowList = Lists.newArrayList();
        for (int i = 1; i <= headerList.size(); i++) {
            dataRowList.add("数据"+i);
        }

        List<List<String>> dataList = Lists.newArrayList();
        for (int i = 1; i <=10; i++) {
            dataList.add(dataRowList);
        }

        ExportExcel ee = new ExportExcel("表格标题", headerList);

        for (int i = 0; i < dataList.size(); i++) {
            Row row = ee.addRow();
            for (int j = 0; j < dataList.get(i).size(); j++) {
                ee.addCell(row, j, dataList.get(i).get(j));
            }
        }

        try {
            ee.writeFile("D:/export.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ee.dispose();

        log.debug("Export success.");
    }

    @Test
    public void importExcel(){
        ImportExcel ei = null;
        try {
            ei = new ImportExcel("D:/export.xlsx", 1);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
            Row row = ei.getRow(i);
            for (int j = 0; j < ei.getLastCellNum(); j++) {
                Object val = ei.getCellValue(row, j);
                System.out.print(val+", ");
            }
            System.out.print("\n");
        }
    }
}
