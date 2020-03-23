import com.alibaba.excel.EasyExcel;
import excelhandler.ConcreteListenerEnum;
import excelhandler.ExcelDataDao;
import excelhandler.FileDaoImpl;
import excelhandler.listener.BankExcelDataListener;
import excelhandler.listener.GeneralExcelDataListener;
import org.apache.commons.lang3.StringUtils;
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

    private ConcreteListenerEnum concreteListenerEnum;
    private String excelPath;
    private ExcelDataDao excelDataDao;

    ExcelHandler(ConcreteListenerEnum concreteListenerEnum, String excelPath, ExcelDataDao excelDataDao) {
        this.concreteListenerEnum = concreteListenerEnum;
        this.excelPath = excelPath;
        this.excelDataDao = excelDataDao;
    }

    public void ParseExcelSetUp(){

        if (ConcreteListenerEnum.BANK.equals(concreteListenerEnum)){
//            String excelPath = "/Users/zhangpingzuan/Desktop/集团客户清单/集团客户清单数据.xlsx";
            EasyExcel.read(excelPath, ExcelDataImpl.class, new BankExcelDataListener(excelDataDao)).sheet().doRead();

        } else if (ConcreteListenerEnum.GENERAL.equals(concreteListenerEnum)) {
//            String excelPath = "/Users/zhangpingzuan/Desktop/集团客户清单/通用数据.xlsx";
            EasyExcel.read(excelPath, CustomTreeNode.class, new GeneralExcelDataListener(excelDataDao)).sheet().doRead();
        }
    }



}
