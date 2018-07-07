package selector;

import util.FileHandle;

import java.util.HashSet;
import java.util.Set;

/**
 * 可视化
 */
public class Graphviz {

    /**
     * 可视化树形图
     *
     * @param leaves       叶节点
     * @param isHorizontal 摆放方式
     * @param filePath     存储文件路径
     * @param top          输出前top个结果
     */
    public static void visual(Node[] leaves, boolean isHorizontal, String filePath, int featureNumber, String[] featureNames, int top) {

        String string = "digraph FeatureTree {\n";
        if (isHorizontal) string += "rankdir = LR\n";
        Set<Node> nodes = new HashSet<>();
        Set<String> links = new HashSet<>();
        int[] frequency = new int[featureNumber];
        int count = 0;
        for (Node leaf : leaves) {
            //计数，判断是否输出前top个结果
            if (count >= top) break;
            count++;

            //表示路径性能的节点
            Node performance = new Node(leaf);
            leaf.addChild(performance);
            nodes.add(performance);
            links.add(" " + leaf.getId() + " -> " + performance.getId() + "\n");

            Node child = leaf;
            Node parent = child.getParent();
            while (parent != null) {

                frequency[child.getFeatureId()] += 1;
                if (count == 1) child.setBest(true);  //设置最好性能的节点
                nodes.add(parent);
                nodes.add(child);
                links.add(" " + parent.getId() + " -> " + child.getId() + "\n");
                // 更新父子关系
                child = parent;
                parent = child.getParent();
            }
        }
        //生成dot文件内容
        for (Node node : nodes) {
            string += node.getId();
            string += "[ ";
            string += "label = \"" + node.getName() + "\", ";
            if (node.getChildren().size() == 0)
                string += " color = green, style = filled, fillcolor = limegreen, shape = box";
            if (node.getName().equals("null"))
                string += " color = pink, style = filled, fillcolor = pink, shape = doublecircle";
            if (node.isBest())
                string += " style = filled, fillcolor = cyan";
            string += "]\n";
        }
        for (String link : links) string += link;
        string += "}";
        FileHandle.writeStringToFile(filePath + ".dot", string); // 写入文件
        drawGraph(filePath, "emf");

        //输出叶子节点 特征频率
        System.out.println("Top " + top + " paths:");
        for (int i = 0; i < top; i++) System.out.println(leaves[i]);
        System.out.println("======================================================");
        System.out.println("Feature frequency in Top " + top + " paths:");
        for (int i = 0; i < frequency.length; i++) System.out.println(featureNames[i] + ": \t" + frequency[i]);
    }

    public static void drawGraph(String filePath, String type) {
        String cmd = "cmd /c start dot -T " + type + " " + filePath + ".dot -o " + filePath + "." + type;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            int i = ps.exitValue();
            if (i != 0) {
                System.out.println("Graph Export Error!");
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}