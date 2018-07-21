package util;

import java.util.Arrays;
import java.util.Comparator;


/**
 * util.Ranking: 对revisions按照某个特征值进行排序
 */
public class Ranking {

    /**
     * 根据特征进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByFeature(Bucket bucket, Integer... features) {
        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);
        rank(inducing, features);
        rank(notInducing, features);
        result = adjust(inducing, notInducing, features);
        return result;
    }


    /**
     * 排序某类特征
     *
     * @param features
     * @param feature
     */
    public static void rank(Feature[][] features, Integer... feature) {
        for (int i = 0; i < features.length; i++) {
            Arrays.sort(features[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double a1 = 1.0;
                    Double a2 = 1.0;
                    for (int f : feature) {
                        a1 *= o1.getValue(f);
                        a2 *= o2.getValue(f);
                    }
                    return a2.compareTo(a1);
                }
            });
        }
    }
    /**
     * 调整位置 Pos
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
                            ind *= inducing[i][j].getValue(feature);
                            notInd *= notInducing[i][k].getValue(feature);
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
}
