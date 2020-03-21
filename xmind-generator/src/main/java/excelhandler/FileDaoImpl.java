package excelhandler;

import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 17:19
 * @description
 */
public class FileDaoImpl implements ExcelDataDao {

    private File file;

    public FileDaoImpl(File file) {
        this.file = file;
    }

    @Override
    public void save(List<ExcelData> excelDataList) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件失败");
            }
        }
        if (file.canWrite()) {
            try {
                FileUtils.write(this.file, JSONObject.toJSONString(constructTree(excelDataList)), "utf-8", false);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("处理失败");
            }
        } else System.out.println("不可写入数据!! 请查看文件：" + file.getAbsolutePath());
    }

    private ExcelData constructTree(List<ExcelData> excelDataList){

        // 构造根树
        ExcelData root = excelDataList.get(0);
        root.setLevel("0");

        // 构造第二层
        List<String> groupStrings = excelDataList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .map(ExcelData::getGroup).distinct().collect(Collectors.toList());
        List<ExcelData>  groupExcelDatas = groupStrings.stream().map(s -> ExcelData.builder()
                .level(String.valueOf(groupStrings.indexOf(s) + 1))
                .clientName(s).group(root.getClientName())
                .build()
        ).collect(Collectors.toList());
        root.setChildren(groupExcelDatas);

        // 构造第三层
        Map<String, ExcelData> groupExcelDatasMap = groupExcelDatas.stream()
                .collect(toMap(ExcelData::getClientName, Function.identity()));
        Map<String, List<ExcelData>> third = excelDataList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .collect(groupingBy(ExcelData::getGroup));
        groupExcelDatasMap.forEach((key, value) -> {
            if (!third.containsKey(key))
                return;
            value.setChildren(third.get(key));
        });

        // 构造第四层
        Map<String, ExcelData> thirdDatas = excelDataList.stream()
                .filter(excelData -> StringUtils.isNotEmpty(excelData.getGroup()))
                .collect(toMap(ExcelData::getLevel, Function.identity()));
        Map<String,List<ExcelData>> forthDatas = excelDataList.stream()
                .filter(excelData -> StringUtils.split(excelData.getLevel(),'.').length == 3)
                .collect(groupingBy(excelData -> StringUtils.substring(excelData.getLevel(), 0 , excelData.getLevel().lastIndexOf('.'))));
        thirdDatas.forEach((key, value) -> {
            if (!forthDatas.containsKey(key))
                return;
            value.setChildren(forthDatas.get(key));
        });
        return root;
    }

}
