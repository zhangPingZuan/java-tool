import excelhandler.ConcreteListenerEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-23 12:32
 * @description
 */
public class XmindMain {

    public static void main(String[] args) {
        if (args.length == 0)
            throw new IllegalArgumentException("传入参数不正确");

        // 读取excel文件
        String excelFilePath = args[0];
        File file = new File(excelFilePath);
        if (!file.exists() && file.canRead()) {
            throw new IllegalArgumentException("excel文件不存在或者无法访问");
        }

        String outPut = args.length == 2 ? args[1] : StringUtils.substring(excelFilePath, 0, excelFilePath.indexOf('.')) + ".xmind";
        ConcreteListenerEnum concreteListenerEnum = args.length == 3 ? ConcreteListenerEnum.valueOf(args[2]) : ConcreteListenerEnum.GENERAL;
        XmindGenerator xmindGenerator = new XmindGenerator(outPut);
        ExcelHandler excelHandler = new ExcelHandler(concreteListenerEnum, excelFilePath, xmindGenerator);
        excelHandler.ParseExcelSetUp();
        xmindGenerator.setUpGenerator();
    }

}
