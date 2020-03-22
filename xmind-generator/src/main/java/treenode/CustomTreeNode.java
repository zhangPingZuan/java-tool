package treenode;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Snow
 * @version 1.0
 * @date 2020-03-22 20:12
 * @description
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomTreeNode {

    private String level;

    private String nodeData;

    private List<CustomTreeNode> children = new ArrayList<>();
}
