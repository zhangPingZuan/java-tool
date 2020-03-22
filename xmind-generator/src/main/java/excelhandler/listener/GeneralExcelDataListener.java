package excelhandler.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import excelhandler.ExcelDataDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import treenode.CustomTreeNode;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-22 19:02
 * @description
 */
public class GeneralExcelDataListener extends AnalysisEventListener<CustomTreeNode> {

    private List<CustomTreeNode> customTreeNodes = new ArrayList<>();

    private ExcelDataDao excelDataDao;

    public GeneralExcelDataListener(ExcelDataDao excelDataDao) {
        this.excelDataDao = excelDataDao;
    }

    @Override
    public void invoke(CustomTreeNode treeNode, AnalysisContext analysisContext) {
        System.out.println("解析到一条数据:" + JSONObject.toJSONString(treeNode));
        customTreeNodes.add(treeNode);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        excelDataDao.save(constructTree(customTreeNodes));
    }

    private CustomTreeNode constructTree(List<CustomTreeNode> treeNodes){

        if (CollectionUtils.isEmpty(treeNodes))
            throw new IllegalArgumentException("传入的excel表格无法解析数据！！！");

        Map<String, CustomTreeNode> map = treeNodes.stream().collect(toMap(CustomTreeNode::getLevel, Function.identity()));
        treeNodes.forEach(treeNode -> {
            if (treeNode.getLevel() == null || treeNode.getLevel().equals("0") || treeNode.getLevel().equals(""))
                return;
            String parentLevel = StringUtils.substring(treeNode.getLevel(), 0, StringUtils.lastIndexOf(treeNode.getLevel(), '-'));
            // 各子节点放入到父级节点
            if (map.containsKey(parentLevel))
                map.get(parentLevel).getChildren().add(treeNode);
            else if (parentLevel.equals("")){ // 放入到特殊的根节点
                if (map.containsKey(null))
                    map.get(null).getChildren().add(treeNode);
                else if (map.containsKey("0"))
                    map.get("0").getChildren().add(treeNode);
                else if (map.containsKey(""))
                    map.get("").getChildren().add(treeNode);
            }
        });
        if (map.containsKey(null))
            return map.get(null);
        else return map.get("0");
    }
}
