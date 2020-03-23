import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSONObject;
import excelhandler.ConcreteListenerEnum;
import excelhandler.ExcelDataDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.xmind.core.*;
import treenode.CustomTreeNode;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 15:22
 * @description
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class XmindGenerator implements ExcelDataDao {

    private String outPut;

    private CustomTreeNode root;

    public XmindGenerator(String outPut){
        this.outPut = outPut;
    }


    public void setUpGenerator() {
        try {
            // 创建思维导图的工作空间
            IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
            IWorkbook workbook = workbookBuilder.createWorkbook();

            // 获得默认sheet
            ISheet primarySheet = workbook.getPrimarySheet();
            ITopic rootTopic = primarySheet.getRootTopic();
            rootTopic.setTitleText(root.getNodeData());
            new XmindGenerator().handleTree(root, rootTopic, workbook);

            // 保存
            workbook.save(outPut);
        } catch (Exception e) {
            System.out.println("生成xmind失败");
        }

    }

    private void handleTree(CustomTreeNode data, ITopic topic, IWorkbook workbook) {
        //说明是叶子节点
        if (CollectionUtils.isEmpty(data.getChildren())) {
            return;
        }
        for (CustomTreeNode customTreeNode : data.getChildren()) {
            ITopic sub = workbook.createTopic();
            sub.setTitleText(customTreeNode.getNodeData());
            topic.add(sub, ITopic.ATTACHED);
            handleTree(customTreeNode, sub, workbook);
        }
    }

    @Override
    public void save(CustomTreeNode root) {
        this.root = root;
    }
}
