import com.alibaba.excel.EasyExcel;
import excelhandler.ConcreteListenerEnum;
import excelhandler.ExcelDataDao;
import excelhandler.FileDaoImpl;
import excelhandler.listener.BankExcelDataListener;
import excelhandler.listener.GeneralExcelDataListener;
import treenode.CustomTreeNode;
import treenode.ExcelDataImpl;

import java.io.File;


/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 16:46
 * @description
 */
public class ExcelHandler {

    public static void main(String[] args) {

        new ExcelHandler(ConcreteListenerEnum.BANK);

    }

    ExcelHandler(ConcreteListenerEnum concreteListenerEnum) {
        if (ConcreteListenerEnum.BANK.equals(concreteListenerEnum)){
            String excelPath = "/Users/zhangpingzuan/Desktop/集团客户清单/集团客户清单数据.xlsx";

            String fileName = "数据test";
            File file = new File(ExcelHandler.class.getResource("/").getPath() + "/" + fileName + ".txt");
            ExcelDataDao excelDataDao = new FileDaoImpl(file);
            EasyExcel.read(excelPath, ExcelDataImpl.class, new BankExcelDataListener(excelDataDao)).sheet().doRead();

        } else if (ConcreteListenerEnum.GENERAL.equals(concreteListenerEnum)) {
            String excelPath = "/Users/zhangpingzuan/Desktop/集团客户清单/通用数据.xlsx";

            String fileName = "通用数据";
            File file = new File(ExcelHandler.class.getResource("/").getPath() + "/" + fileName + ".txt");
            ExcelDataDao excelDataDao = new FileDaoImpl(file);
            EasyExcel.read(excelPath, CustomTreeNode.class, new GeneralExcelDataListener(excelDataDao)).sheet().doRead();
        }
    }


}
