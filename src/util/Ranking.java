package util;

import java.util.Arrays;
import java.util.Comparator;

/**
 * util.Ranking: 对revisions按照某个特征值进行排序
 */
public class Ranking {

    /**
     * 根据一个属性进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByFeature(Bucket bucket, int featureName) {
        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);
        rank(inducing, featureName);
        rank(notInducing, featureName);
        result = adjust(inducing, notInducing, featureName);
        return result;
    }

    /**
     * 根据其他属性+组件进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByPi(Bucket bucket, int f1, int f2, double threshold) {
        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);
        rank2(inducing, f1, f2, threshold);
        rank2(notInducing, f1, f2, threshold);
        result = adjust2(inducing, notInducing, f1, f2, threshold);
        return result;
    }

    /**
     * 根据其他属性+组件进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByPid(Bucket bucket, int f1, int f2, int f3) {
        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);
        rank3(inducing, f1, f2, f3);
        rank3(notInducing, f1, f2, f3);
        result = adjust3(inducing, notInducing, f1, f2, f3);
        return result;
    }

    /**
     * 调整位置 Pos
     *
     * @param inducing
     * @param notInducing
     * @param featureName
     * @return
     */
    private static Feature[][] adjust(Feature[][] inducing, Feature[][] notInducing, int featureName) {
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
                        double ind = inducing[i][j].getValue(featureName);
                        double notInd = notInducing[i][k].getValue(featureName);
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 调整位置 Pos
     *
     * @param inducing
     * @param notInducing
     * @param f1,f2
     * @return
     */
    private static Feature[][] adjust2(Feature[][] inducing, Feature[][] notInducing, int f1, int f2, double threshold) {
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
                        double c1 = inducing[i][j].getValue(f2);
                        double c2 = notInducing[i][k].getValue(f2);

                        double ind = inducing[i][j].getValue(f1) * c1;
                        double notInd = notInducing[i][k].getValue(f1) * c2;
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 调整位置 Pos
     *
     * @param inducing
     * @param notInducing
     * @param f1,f2
     * @return
     */
    private static Feature[][] adjust3(Feature[][] inducing, Feature[][] notInducing, int f1, int f2, int f3) {
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
                        double ind = inducing[i][j].getValue(f1) * inducing[i][j].getValue(f2) * inducing[i][j].getValue(f3);
                        double notInd = notInducing[i][k].getValue(f1) * notInducing[i][k].getValue(f2) * notInducing[i][k].getValue(f3);
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
            for (int j = 0; j < features[i].length; j++) if (features[i][j].isInducing() == isInducing) count[i]++;

        Feature[][] result = new Feature[features.length][];
        for (int i = 0; i < features.length; i++) {
            result[i] = new Feature[count[i]];
            for (int j = 0, k = 0; j < features[i].length; j++) {
                if (features[i][j].isInducing() == isInducing) {
                    result[i][k++] = features[i][j];
                }
            }
        }
        return result;
    }

    /**
     * 排序某类特征
     *
     * @param features
     * @param featureName
     */
    public static void rank(Feature[][] features, int featureName) {
        for (int i = 0; i < features.length; i++) {
            Arrays.sort(features[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double f1 = o1.getValue(featureName);
                    Double f2 = o2.getValue(featureName);
                    return f2.compareTo(f1);
                }
            });
        }
    }

    /**
     * 排序某类特征
     *
     * @param features
     * @param f1,f2
     */
    public static void rank2(Feature[][] features, int f1, int f2, double threshold) {
        for (int i = 0; i < features.length; i++) {
            Arrays.sort(features[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    double c1 = o1.getValue(f2);
                    double c2 = o2.getValue(f2);
                    Double a1 = o1.getValue(f1) * c1;
                    Double a2 = o2.getValue(f1) * c2;
                    return a2.compareTo(a1);
                }
            });
        }
    }

    /**
     * 排序某类特征
     *
     * @param features
     * @param f1,f2
     */
    public static void rank3(Feature[][] features, int f1, int f2, int f3) {
        for (int i = 0; i < features.length; i++) {
            Arrays.sort(features[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double a1 = o1.getValue(f1) * o1.getValue(f2) * o1.getValue(f3);
                    Double a2 = o2.getValue(f1) * o2.getValue(f2) * o2.getValue(f3);
                    return a2.compareTo(a1);
                }
            });
        }
    }
}
