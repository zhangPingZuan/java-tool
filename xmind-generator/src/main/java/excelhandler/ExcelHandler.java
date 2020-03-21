package excelhandler;

import com.alibaba.excel.EasyExcel;

import java.io.File;


/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 16:46
 * @description
 */
public class ExcelHandler {

    public static void main(String[] args) {

        String excelPath = "/Users/zhangpingzuan/Desktop/集团客户清单/集团客户清单数据.xlsx";

        String fileName = "数据test";
        File file = new File(ExcelHandler.class.getResource("/").getPath() + "/" + fileName + ".txt");
        ExcelDataDao excelDataDao = new FileDaoImpl(file);
        EasyExcel.read(excelPath, ExcelData.class, new ExcelDataListener(excelDataDao)).sheet().doRead();

    }

}
