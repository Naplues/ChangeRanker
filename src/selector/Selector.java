package selector;

import util.Feature;

import java.util.*;

/**
 * 有效特征选择
 */
public class Selector {

    /**
     * 开始选择
     *
     * @param featureNumber
     */
    public void start(int featureNumber, String filePath, int neededFeatureNumber, double threshold) throws Exception {
        Node root = new Node(featureNumber);
        explore(root, neededFeatureNumber);
        System.out.println("Explore Finish.");

        // 获取叶子节点
        List<Node> leaves = new ArrayList<>();
        DFS(root, leaves, threshold);
        Node[] result = Node.toNode(leaves.toArray());
        Arrays.sort(result, new Comparator<Node>() {
            public int compare(Node o1, Node o2) {
                Double a1 = o1.getPerformance();
                Double a2 = o2.getPerformance();
                return a2.compareTo(a1);
            }
        });

        //输出叶子节点
        for (int i = 0; i < result.length; i++) System.out.println(result[i]);
        Graphviz.visual(leaves, false, filePath);
    }


    /**
     * 探索新特征
     *
     * @param parent
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
                Node newNode = new Node(getFeatureName(candidate), parent, usedSet, candidatesSet, newPerformance);
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

    public String getFeatureName(Object valueIndex) {
        switch ((Integer) valueIndex) {
            case 0:
                return "NAF";
            case 1:
                return "CC";
            case 2:
                return "RLOCC";
            case 3:
                return "RLOAC";
            case 4:
                return "RLODC";
            case 5:
                return "IADCP";
            case 6:
                return "ITDCR";
            case 7:
                return "RF";
            case 8:
                return "IBF";
            case 9:
                return "IADCL";
            default:
                return "IADCP";
        }
    }
}
