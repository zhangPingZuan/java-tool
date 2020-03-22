package excelhandler.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import excelhandler.ExcelDataDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import treenode.ExcelDataImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 16:59
 * @description
 */
@NoArgsConstructor
@AllArgsConstructor
public class BankExcelDataListener extends AnalysisEventListener<ExcelDataImpl> {

    List<ExcelDataImpl> excelDataImplList = new ArrayList<>();

    private ExcelDataDao excelDataDao;

    public BankExcelDataListener(ExcelDataDao excelDataDao) {
        this.excelDataDao = excelDataDao;
    }


    @Override
    public void invoke(ExcelDataImpl excelDataImpl, AnalysisContext analysisContext) {

        // 做一下数据清洗
        if (excelDataImpl.getLevel().length() > 10) {
            int i = StringUtils.indexOf(excelDataImpl.getLevel(), '0');
            excelDataImpl.setLevel(StringUtils.substring(excelDataImpl.getLevel(), 0 , i));
        }
        System.out.println("解析到一条数据:" + JSONObject.toJSONString(excelDataImpl));
        excelDataImplList.add(excelDataImpl);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("所有数据解析完成！");
        if (excelDataDao != null)
            excelDataDao.save(constructTree(excelDataImplList));
    }

    private ExcelDataImpl constructTree(List<ExcelDataImpl> excelDataImplList){

        // 构造根树
        ExcelDataImpl root = excelDataImplList.get(0);
        root.setLevel("0");

        // 构造第二层
        List<String> groupStrings = excelDataImplList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .map(ExcelDataImpl::getGroup).distinct().collect(Collectors.toList());
        List<ExcelDataImpl> groupExcelDataImpls = groupStrings.stream().map(s -> {
            ExcelDataImpl e = new ExcelDataImpl();
            e.setLevel(String.valueOf(groupStrings.indexOf(s) + 1));
            e.setGroup(root.getNodeData());
            e.setNodeData(s);
            return e;
        }).collect(Collectors.toList());
        root.setChildren(new ArrayList<>(groupExcelDataImpls));

        // 构造第三层
        Map<String, ExcelDataImpl> groupExcelDatasMap = groupExcelDataImpls.stream()
                .collect(toMap(ExcelDataImpl::getNodeData, Function.identity()));
        Map<String, List<ExcelDataImpl>> third = excelDataImplList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .collect(groupingBy(ExcelDataImpl::getGroup));
        groupExcelDatasMap.forEach((key, value) -> {
            if (!third.containsKey(key))
                return;
            value.setChildren(new ArrayList<>(third.get(key)));
        });

        // 构造第四层
        Map<String, ExcelDataImpl> thirdDatas = excelDataImplList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .collect(toMap(ExcelDataImpl::getLevel, Function.identity()));
        Map<String,List<ExcelDataImpl>> forthDatas = excelDataImplList.stream()
                .filter(excelData -> StringUtils.split(excelData.getLevel(),'.').length == 3)
                .collect(groupingBy(excelData -> StringUtils.substring(excelData.getLevel(), 0 , excelData.getLevel().lastIndexOf('.'))));
        thirdDatas.forEach((key, value) -> {
            if (!forthDatas.containsKey(key))
                return;
            value.setChildren(new ArrayList<>(forthDatas.get(key)));
        });
        return root;
    }
}
