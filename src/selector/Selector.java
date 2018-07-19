package selector;

import java.util.*;

/**
 * 有效特征选择
 */
public class Selector {
    //选项值: 表示使用全部组合结果
    public static final int ALL = -1;

    /**
     * 开始选择
     *
     * @param featureNumber
     */
    public void start(int featureNumber, String filePath, int neededFeatureNumber, double threshold, boolean isHorizontal, int top) {
        Node root = new Node(featureNumber);  //创建根节点
        explore(root, neededFeatureNumber);  //探索特征组合
        System.out.println("Explore Finish.");

        // 获取叶子节点，并根据性能对叶子节点排序
        List<Node> leaves = new ArrayList<>();
        DFS(root, leaves, threshold);
        Node[] result = Node.toNode(leaves.toArray());
        Arrays.sort(result, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                // 两种组合的性能
                Double p1 = o1.getPerformance();
                Double p2 = o2.getPerformance();
                // 两种组合的特征数
                Integer f1 = o1.getFeatureUsed().size();
                Integer f2 = o2.getFeatureUsed().size();
                int pr = p2.compareTo(p1); //性能比较结果,逆排
                if (pr == 0) pr = f1.compareTo(f2); //特征数比较结果, 性能值相同时使用组合特征数较少的排列靠前,顺排。
                return pr;
            }
        });

        String[] featureNames = new String[featureNumber];
        for (int i = 0; i < featureNumber; i++) featureNames[i] = getFeatureName(i);
        if (top == ALL) top = leaves.size();  //当未指定选择数目时，使用全部叶节点。修正全部叶节点个数
        Graphviz.visual(result, isHorizontal, filePath, featureNumber, featureNames, top);
    }


    /**
     * 探索新特征
     *
     * @param parent
     * @param neededFeatureNumber
     */
    public void explore(Node parent, int neededFeatureNumber) {
        // 获取候选特征集合
        Set<Object> candidatesSet = parent.getFeatureCandidates();
        Object[] candidates = candidatesSet.toArray();
        if (candidates.length == 0) {
            return;
        }
        for (Object candidate : candidates) {
            // 获取新的特征集合
            Set<Object> usedSet = parent.getFeatureUsed();
            usedSet.add(candidate);
            Object[] currentFeatures = usedSet.toArray();

            //获取旧的和新的性能值
            double oldPerformance = parent.getPerformance();
            double newPerformance = getValue(Node.toInteger(currentFeatures));

            //新性能较好，继续
            if (newPerformance >= oldPerformance) {
                candidatesSet.remove(candidate); //更新候选集
                Node newNode = new Node(getFeatureName(candidate), candidate, parent, usedSet, candidatesSet, newPerformance);
                parent.addChild(newNode);
                if (newNode.getFeatureUsed().size() < neededFeatureNumber)
                    explore(newNode, neededFeatureNumber);  //探索子节点
            } else {
                usedSet.remove(candidate);
            }
        }
    }

    /**
     * 深度优先遍历
     *
     * @param node
     * @param leaves
     * @param threshold
     */
    public static void DFS(Node node, List<Node> leaves, double threshold) {
        List<Node> children = node.getChildren();
        if (children.size() == 0 && node.getPerformance() > threshold)
            leaves.add(node);
        for (int i = 0; i < children.size(); i++) DFS(children.get(i), leaves, threshold);
    }

    /**
     * 获取Metric值
     *
     * @param features
     * @return
     */

    public double getValue(Integer[] features) {
        return .0;
    }

    /**
     * 获取索引特征名称
     *
     * @param valueIndex
     * @return
     */
    public String getFeatureName(Object valueIndex) {
        return "null";
    }
}