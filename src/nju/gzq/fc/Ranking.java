package nju.gzq.fc;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Ranking: 对revisions按照某个特征值进行排序
 */
public class Ranking {

    // combination approach (MULTIPLE x /SUMMATION +)
    public static final int MULTIPLE = 0;
    public static final int SUMMATION = 1;

    // ranking approach (ASC/DESC)
    public static final int RANK_ASC = 0;
    public static final int RANK_DESC = 1;

    /**
     * 根据指定特征组合进行排序
     *
     * @param project     待处理项目: 6.7
     * @param combination 组合方式: 'x' or '+'
     * @param ranking     排序方式: 升序 or 降序
     * @param features    使用到的特征
     * @return
     */
    public static Feature[][] rankByFeature(Project project, int combination, int ranking, Integer... features) {
        Feature[][] result = project.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);
        rank(inducing, combination, ranking, features);
        rank(notInducing, combination, ranking, features);
        result = adjust(inducing, notInducing, features);
        return result;
    }


    /**
     * 排序某类特征
     *
     * @param features
     * @param feature
     */
    public static void rank(Feature[][] features, int combination, int ranking, Integer... feature) {
        for (int i = 0; i < features.length; i++) {
            Arrays.sort(features[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double a1 = 1., a2 = 1.;

                    // combination approach
                    if (combination == MULTIPLE) {
                        for (int f : feature) {
                            a1 *= o1.getValueFromIndex(f);
                            a2 *= o2.getValueFromIndex(f);
                        }
                    } else if (combination == SUMMATION) {
                        a1 = .0;
                        a2 = .0;
                        for (int f : feature) {
                            a1 += o1.getValueFromIndex(f);
                            a2 += o2.getValueFromIndex(f);
                        }
                    }
                    o1.setTemp(a1);
                    o2.setTemp(a2);

                    // ranking approach
                    if (ranking == RANK_ASC) return a1.compareTo(a2);
                    else if (ranking == RANK_DESC) return a2.compareTo(a1);
                    return 0;
                }
            });
        }
    }

    /**
     * 调整位置, 使用最坏情况作为排序结果, 查看方法的最差性能
     * 当inducing changes 和 non-inducing changes 组合值相等时, 将non-inducing changes 排在前面
     *
     * @param inducing
     * @param notInducing
     * @param features
     * @return
     */
    private static Feature[][] adjust(Feature[][] inducing, Feature[][] notInducing, Integer... features) {
        Feature[][] result = new Feature[inducing.length][];

        for (int i = 0; i < result.length; i++) {
            result[i] = new Feature[inducing[i].length + notInducing[i].length];
            for (int j = 0, k = 0, m = 0; m < result[i].length; m++) {
                if (j >= inducing[i].length)
                    result[i][m] = notInducing[i][k++];
                else {
                    if (k >= notInducing[i].length) {
                        result[i][m] = inducing[i][j++];
                    } else {
                        double ind = 1.0;
                        double notInd = 1.0;
                        for (int feature : features) {
                            ind *= inducing[i][j].getValueFromIndex(feature);
                            notInd *= notInducing[i][k].getValueFromIndex(feature);
                        }
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取inducing 和 not inducing 数组
     *
     * @param features
     * @param isInducing
     * @return
     */
    private static Feature[][] getInducing(Feature[][] features, boolean isInducing) {
        int[] count = new int[features.length];
        for (int i = 0; i < features.length; i++)
            for (int j = 0; j < features[i].length; j++) if (features[i][j].isLabel() == isInducing) count[i]++;

        Feature[][] result = new Feature[features.length][];
        for (int i = 0; i < features.length; i++) {
            result[i] = new Feature[count[i]];
            for (int j = 0, k = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel() == isInducing) {
                    result[i][k++] = features[i][j];
                }
            }
        }
        return result;
    }
}
