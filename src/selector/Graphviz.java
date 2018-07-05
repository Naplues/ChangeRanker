package selector;

import util.FileHandle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 可视化
 */
public class Graphviz {

    public static void visual(List<Node> leaves, boolean isHorizontal, String filePath) {
        String string = "digraph FeatureTree {\n";
        if (isHorizontal) string += "rankdir = LR\n";
        Set<Node> nodes = new HashSet<>();
        Set<String> links = new HashSet<>();
        for (Node leaf : leaves) {
            //表示叶子节点的性能
            Node performance = new Node(leaf);
            leaf.addChild(performance);
            nodes.add(performance);
            links.add(" " + leaf.getId() + " -> " + performance.getId() + "\n");

            Node child = leaf;
            Node parent = child.getParent();
            while (parent != null) {
                nodes.add(parent);
                nodes.add(child);
                links.add(" " + parent.getId() + " -> " + child.getId() + "\n");
                // 更新父子关系
                child = parent;
                parent = child.getParent();
            }
        }
        for (Node node : nodes) {
            string += node.getId();
            string += "[ ";
            string += "label = \"" + node.getName() + "\", ";
            if (node.getChildren().size() == 0)
                string += " color = green, style=filled, fillcolor = lightgreen, shape = box";
            string += "]\n";
        }
        for (String link : links) string += link;
        string += "}";
        FileHandle.writeStringToFile(filePath + ".dot", string); // 写入文件
        drawGraph(filePath, "svg");
    }

    public static void drawGraph(String filePath, String type) {
        String cmd = "cmd /c start dot -T " + type + " " + filePath + ".dot -o " + filePath + "." + type;
        System.out.println(cmd);
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            int i = ps.exitValue();
            if (i != 0) {
                System.out.println("导出失败");
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}
