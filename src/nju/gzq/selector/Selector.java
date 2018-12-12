package nju.gzq.selector;

import nju.gzq.plc.Project;

import java.util.*;

/**
 * 有效特征选择
 */
public class Selector {
    //选项值: 表示使用全部组合结果
    private static final int ALL = -1;

    /**
     * 开始选择特征
     *
     * @param trainVersions       训练的版本
     * @param featureNumber       特征数目
     * @param neededFeatureNumber 最多组合特征数
     * @param threshold           性能阈值
     * @param top                 输出前top个结果
     */
    public final Integer[] start(List<String> trainVersions, int featureNumber, int neededFeatureNumber, double threshold, int top) {
        Node root = new Node(featureNumber);  //创建根节点
        explore(trainVersions, root, neededFeatureNumber);  //探索特征组合
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
        //当未指定选择返回结果数或者选择的结果数大于实际生成的值时，使用全部叶节点。修正全部叶节点个数
        if (top == ALL || top > leaves.size()) top = leaves.size();

        // 可视化
        //Graphviz.visual(result, true, "./out", "svg", featureNames, top);

        // 返回Top@k中使用特征数最少的组合
        int bestIndex = top - 1; //最优组合索引
        for (int i = top - 1, min = result[top - 1].getFeatureUsed().size(); i >= 0; i--) {
            if (result[i].getFeatureUsed().size() <= min) {
                min = result[i].getFeatureUsed().size();
                bestIndex = i;
            }
        }

        Set<Object> bestSet = result[bestIndex].getFeatureUsed();
        Integer[] bestCombination = new Integer[bestSet.size()];
        int i = 0;
        System.out.print("Best Feature Combination: ");
        for (Object featureIndex : bestSet) {
            bestCombination[i++] = (Integer) featureIndex;
            System.out.print(featureIndex + " ");
        }
        System.out.println();
        return bestCombination;
    }

    /**
     * 探索新特征
     *
     * @param parent
     */
    public final void explore(List<String> trainVersions, Node parent, int neededFeatureNumber) {
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
            double newPerformance = getValue(trainVersions, Node.toInteger(currentFeatures));

            //新性能较好，继续
            if (newPerformance >= oldPerformance) {
                candidatesSet.remove(candidate); //更新候选集
                Node newNode = new Node(getFeatureName(candidate), candidate, parent, usedSet, candidatesSet, newPerformance);
                parent.addChild(newNode);
                if (newNode.getFeatureUsed().size() < neededFeatureNumber)
                    explore(trainVersions, newNode, neededFeatureNumber);  //探索子节点
            } else {
                usedSet.remove(candidate);
            }
        }
    }

    /**
     * 深度优先遍历特征树, 找到树中的所有叶子节点
     *
     * @param node      父节点
     * @param leaves    叶子节点列表, 初始为空, 遍历完成后存储所有的叶节点
     * @param threshold 阈值
     */
    public final static void DFS(Node node, List<Node> leaves, double threshold) {
        List<Node> children = node.getChildren();
        if (children.size() == 0 && node.getPerformance() > threshold)
            leaves.add(node);
        for (int i = 0; i < children.size(); i++) DFS(children.get(i), leaves, threshold);
    }

    //////////////////////////////////////////////////// 重写方法 ///////////////////////////////////////////////////////

    /**
     * 重写方法: 获取指定的Metric值
     *
     * @param features 特征列表
     * @return 组合后特征的度量值
     */

    public double getValue(List<String> trainVersions, Integer[] features) {
        return .0;
    }

    /**
     * 重写方法: 获取指定索引的特征名称
     *
     * @param valueIndex 特征索引
     * @return 特征名称
     */
    public String getFeatureName(Object valueIndex) {
        return Project.getFeatureNames(valueIndex);
    }
}