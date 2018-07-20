package selector;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 结点：表示一个特征
 * <p>
 * 1. 使用的特征集合结点
 * 2. 剩余的特征集合结点
 * 2. 性能分数
 * 3. 父节点
 * 4. 子节点
 */
public class Node {
    public static Integer ID = 0;
    private Integer id;
    private String name;
    private int featureId;
    private Node parent;
    private boolean best;
    private List<Node> children;

    private Set<Object> featureUsed;
    private Set<Object> featureCandidates;
    private double performance;


    public Node() {
        id = ID++;
        name = "null";
        best = false;
        parent = null;
        children = new ArrayList<>();
        featureUsed = null;
        featureCandidates = null;
        performance = 0;
    }

    public Node(int featureNumber) {
        this();
        Set<Object> initialSet = new HashSet<>();
        for (int i = 0; i < featureNumber; i++) {
            initialSet.add(new Integer(i));
        }
        this.featureUsed = new HashSet<>();
        this.featureCandidates = initialSet;
    }

    /**
     * 性能节点构造器
     *
     * @param parent
     */
    public Node(Node parent) {
        this();
        this.name = new DecimalFormat("0.000").format(parent.getPerformance());
        this.parent = parent;
    }

    /**
     * 特征节点构造器
     *
     * @param name
     * @param featureId
     * @param parent
     * @param featureUsed
     * @param featureCandidates
     * @param performance
     */
    public Node(String name, Object featureId, Node parent, Set<Object> featureUsed, Set<Object> featureCandidates, double performance) {
        this();
        this.name = name;
        this.featureId = (Integer) featureId;
        this.parent = parent;
        this.featureUsed = featureUsed;
        this.featureCandidates = featureCandidates;
        this.performance = performance;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Set<Object> getFeatureCandidates() {
        Set<Object> set = new HashSet<>();
        for (Object o : featureCandidates) {
            set.add(o);
        }
        return set;
    }

    public void setFeatureCandidates(Set<Object> featureCandidates) {
        this.featureCandidates = featureCandidates;
    }

    public double getPerformance() {
        return performance;
    }

    public void setPerformance(double performance) {
        this.performance = performance;
    }

    public Set<Object> getFeatureUsed() {
        Set<Object> set = new HashSet<>();
        for (Object o : featureUsed) {
            set.add(o);
        }
        return set;
    }

    public void setFeatureUsed(Set<Object> featureUsed) {

        this.featureUsed = featureUsed;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public boolean isBest() {
        return best;
    }

    public void setBest(boolean best) {
        this.best = best;
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String toString() {
        String text = "Features: ";
        for (Object f : featureUsed) {
            text += f + " ";
        }
        text += " => " + new DecimalFormat("0.000").format(performance);
        return text;
    }

    public static Integer[] toInteger(Object[] l) {
        Integer[] list = new Integer[l.length];
        for (int i = 0; i < l.length; i++) {
            list[i] = (Integer) l[i];
        }
        return list;
    }

    public static Node[] toNode(Object[] l) {
        Node[] list = new Node[l.length];
        for (int i = 0; i < l.length; i++) {
            list[i] = (Node) l[i];
        }
        return list;
    }

    public static String makeName(Set<Object> set) {
        String name = "\"";
        for (Object s : set) name += s + " ";
        name += "\"";
        return name;
    }
}