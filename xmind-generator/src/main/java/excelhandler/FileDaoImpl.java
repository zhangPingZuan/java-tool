package excelhandler;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import treenode.CustomTreeNode;

import java.io.File;
import java.io.IOException;

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
    public void save(CustomTreeNode root) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件失败");
            }
        }
        if (file.canWrite()) {
            try {
                FileUtils.write(this.file, JSONObject.toJSONString(root), "utf-8", false);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("处理失败");
            }
        } else System.out.println("不可写入数据!! 请查看文件：" + file.getAbsolutePath());
    }



}
