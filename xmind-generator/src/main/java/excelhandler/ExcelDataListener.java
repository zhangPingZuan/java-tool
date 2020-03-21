package excelhandler;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 16:59
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDataListener extends AnalysisEventListener<ExcelData> {

    List<ExcelData> excelDataList = new ArrayList<>();

    private ExcelDataDao excelDataDao;

    public ExcelDataListener(ExcelDataDao excelDataDao) {
        this.excelDataDao = excelDataDao;
    }


    @Override
    public void invoke(ExcelData excelData, AnalysisContext analysisContext) {

        // 做一下数据清洗
        if (excelData.getLevel().length() > 10) {
            int i = StringUtils.indexOf(excelData.getLevel(), '0');
            excelData.setLevel(StringUtils.substring(excelData.getLevel(), 0 , i));
        }
        System.out.println("解析到一条数据:" + JSONObject.toJSONString(excelData));
        excelDataList.add(excelData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("所有数据解析完成！");
        if (excelDataDao != null)
            excelDataDao.save(excelDataList);
    }
}
