package selector;

import util.FileHandle;

import java.util.HashSet;
import java.util.Set;

/**
 * 可视化类
 */
public class Graphviz {
    /**
     * 生成图片类型
     */
    public final static String TYPE_SVG = "svg";
    public final static String TYPE_PNG = "png";
    public final static String TYPE_JPG = "jpg";

    /**
     * 可视化树形图
     *
     * @param leaves               叶节点集合, 可以根据叶节点探索对应的整条路径
     * @param isHorizontal         结点摆放方式 水平/垂直
     * @param filePath             存储文件路径
     * @param type                 输出文件类型
     * @param featureNames         特征名称
     * @param top                  输出前top个结果
     * @param frequencyInformation 是否显示频率信息
     */
    public static void visual(Node[] leaves, boolean isHorizontal, String filePath, String type, String[] featureNames, int top, boolean frequencyInformation) {

        ///////////////////////////////////////// 1. 收集路径信息( 节点集合和边集合) //////////////////////////////////////
        Set<Node> nodes = new HashSet<>(); //节点集合
        Set<String> links = new HashSet<>(); //边集合
        int[] frequency = new int[featureNames.length];  //特征出现频率数组
        int count = 0; //当前存储的组合数目
        for (Node leaf : leaves) { //处理一条路径
            if (count >= top) break;//计数，判断是否输出前top个组合结果
            count++;
            Node performance = new Node(leaf); //表示路径性能的节点,根据叶节点的分数值构造“性能节点”,该节点仅显示路径分数
            leaf.addChild(performance);  //将性能节点续在路径，末端
            nodes.add(performance);  //将性能节点加入节点集合
            links.add(" " + leaf.getId() + " -> " + performance.getId() + "\n"); //将路径末端的叶节点->性能节点的边加入边集合

            //指定初始的父子节点
            Node child = leaf;
            Node parent = child.getParent();
            // 从叶节点向根节点回溯,将路径上的所有节和边都加入香型的集合
            while (parent != null) {
                frequency[child.getFeatureId()] += 1; //更新相应特征的出现频率
                if (count == 1) child.setBest(true);  //设置最好性能的节点
                nodes.add(parent);
                nodes.add(child);
                links.add(" " + parent.getId() + " -> " + child.getId() + "\n");
                // 更新父子关系
                child = parent;
                parent = child.getParent();
            }
        }

        /////////////////////////////////////////// 2. 生成dot文件内容 //////////////////////////////////////////////////
        String string = "digraph FeatureTree {\n";
        if (isHorizontal) string += "rankdir = LR\n";
        // 生成节点信息
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
        // 生成边信息
        for (String link : links) string += link;

        //////////////////////////////////////////// 3. 输出叶子节点及特征频率值 //////////////////////////////////////////
        String summary = "";
        summary += "Top " + top + " paths:\n";
        for (int i = 0; i < top; i++) summary += leaves[i] + "\n";
        summary += "=============================\n";
        summary += "Feature frequency in Top " + top + " paths:\n";
        for (int i = 0; i < frequency.length; i++) summary += featureNames[i] + ": \t" + frequency[i] + "\n";
        System.out.println(summary);
        if (frequencyInformation)
            string += "-1 [ label = \"" + summary + "\" shape = square, style = filled, fillcolor = lightgray ]";

        string += "}";
        FileHandle.writeStringToFile(filePath + ".dot", string); // 写入文件


        //////////////////////////////////////////// 4. 读取dot文件进行绘图 //////////////////////////////////////////////
        String cmd = "cmd /c start dot -T " + type + " " + filePath + ".dot -o " + filePath + "." + type;
        try {
            Process ps = Runtime.getRuntime().exec(cmd);
            ps.waitFor();
            int i = ps.exitValue();
            if (i != 0) System.out.println("Graph Export Error!");
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}