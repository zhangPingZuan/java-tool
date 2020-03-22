import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.xmind.core.*;
import treenode.CustomTreeNode;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-20 15:22
 * @description
 */
public class XmindGenerator {

    /**
     * 当前类路径
     */
    public static final String CLASS_PATH = XmindGenerator.class.getResource("/").getPath();
    /**
     * 文件分隔符
     */
    public static final String FILE_SEPARATOR = SystemUtil.getOsInfo().getFileSeparator();

    public static void main(String[] args) throws IOException, CoreException {

        // 读取目录
        String bookName = "数据test";
        String data = FileUtil.readLine(new RandomAccessFile(new File(CLASS_PATH + bookName + ".txt"), "r"), CharsetUtil.CHARSET_UTF_8);

        CustomTreeNode root  = JSONObject.parseObject(data, CustomTreeNode.class);

        // 创建思维导图的工作空间
        IWorkbookBuilder workbookBuilder = Core.getWorkbookBuilder();
        IWorkbook workbook = workbookBuilder.createWorkbook();

        // 获得默认sheet
        ISheet primarySheet = workbook.getPrimarySheet();
        ITopic rootTopic = primarySheet.getRootTopic();
        rootTopic.setTitleText(root.getNodeData());
        new XmindGenerator().handleTree(root,rootTopic, workbook );

        // 保存
        workbook.save(CLASS_PATH + bookName + ".xmind");
    }

    private void handleTree(CustomTreeNode data, ITopic topic, IWorkbook workbook){
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

}
