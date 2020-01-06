package com.jzb.base.office;

import com.jzb.base.io.JzbStreamUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel读写操作
 *
 * @author Chad
 * @date 2019年08月19日
 */
public final class JzbExcelOperater {
    /**
     * 私有构造方法
     */
    private JzbExcelOperater() {
    } // End JzbExcelOperater

    /**
     * 判断文件是否EXCEL
     *
     * @param filePath
     * @return
     */
    public static boolean isExcel(String filePath) {
        return filePath != null && new File(filePath).isFile() && (filePath.endsWith(".xls") || filePath.endsWith(".xlsx"));
    } // End isExcel

    /**
     * 读Excel文件
     *
     * @param filePath 文件路径
     * @return 读取的数据集合
     */
    public static List<Map<Integer, String>> readSheet(String filePath) {
        return readSheet(filePath, 0);
    } // End readSheet

    /**
     * 读Excel文件
     *
     * @param filePath 文件路径
     * @return 读取的数据集合
     */
    public static List<Map<Integer, String>> readSheet(String filePath, int index) {
        List<Map<Integer, String>> result;
        InputStream is = null;
        try {
            if (!isExcel(filePath)) {
                throw new Exception("File is not found.");
            }

            // 打开文件
            is = new FileInputStream(filePath);
            Workbook wb = filePath.endsWith("xls") ? new HSSFWorkbook(is) : new XSSFWorkbook(is);

            // 读第一个SHEET数据
            Sheet sheet = wb.getSheetAt(index);
            int rowSize = sheet.getPhysicalNumberOfRows();
            result = new ArrayList<>(rowSize);

            int colSize = sheet.getRow(0).getPhysicalNumberOfCells();
            // 读取数据。
            for (int i = 0; i < rowSize; i++) {
                Row row = sheet.getRow(i);
                Map<Integer, String> record = new HashMap<>(colSize);
                for (int l = 0; l < colSize; l++) {
                    Cell cell = row.getCell(l);
                    try {
                        String value;
                        if (cell == null){
                            value = "";
                        }else if (cell.getCellType().equals(CellType.NUMERIC)) {
                            DecimalFormat df=new DecimalFormat("0");
                            value =df.format(cell.getNumericCellValue());
//                            value = cell.getNumericCellValue() + "";
                        } else if (cell.getCellType().equals(CellType.FORMULA)) {
                            value = cell.getCellFormula();
                        } else {
                            value = cell.getStringCellValue();
                        }
                        record.put(l, value);
                    } catch (Exception e) {
                        record.put(l, null);
                    }
                }
                result.add(record);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        } finally {
            JzbStreamUtil.closeStream(is);
        }
        return result;
    } // End readSheet

    public static void main(String[] args) {
        List<Map<Integer, String>> records = readSheet("f:/test/test.xlsx");
        for (int i = 0, l = records.size(); i < l; i++) {
            System.out.println(records.get(i).toString());
        }
    }// End readSheet

} // End class JzbExcelOperater
