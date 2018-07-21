package nju.gzq.pid;

import nju.gzq.base.BaseFeature;
import nju.gzq.base.BaseRanking;

/**
 * Ranking: 对revisions按照某个特征值进行排序
 */
public class Ranking extends BaseRanking {

    /**
     * 根据特征进行排序
     * @param bucket
     * @param combination
     * @param ranking
     * @param features
     * @return
     */
    public static BaseFeature[][] rankByFeature(Project bucket, int combination, int ranking, Integer... features) {
        BaseFeature[][] result = bucket.getFeatures();
        BaseFeature[][] inducing = getInducing(result, true);
        BaseFeature[][] notInducing = getInducing(result, false);
        rank(inducing, combination, ranking, features);
        rank(notInducing, combination, ranking, features);
        result = adjust(inducing, notInducing, features);
        return result;
    }

    /**
     * 调整位置 Pos
     *
     * @param inducing
     * @param notInducing
     * @param features
     * @return
     */
    private static BaseFeature[][] adjust(BaseFeature[][] inducing, BaseFeature[][] notInducing, Integer... features) {
        BaseFeature[][] result = new BaseFeature[inducing.length][];

        for (int i = 0; i < result.length; i++) {
            result[i] = new BaseFeature[inducing[i].length + notInducing[i].length];
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
    private static BaseFeature[][] getInducing(BaseFeature[][] features, boolean isInducing) {
        int[] count = new int[features.length];
        for (int i = 0; i < features.length; i++)
            for (int j = 0; j < features[i].length; j++) if (features[i][j].isLabel() == isInducing) count[i]++;

        BaseFeature[][] result = new BaseFeature[features.length][];
        for (int i = 0; i < features.length; i++) {
            result[i] = new BaseFeature[count[i]];
            for (int j = 0, k = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel() == isInducing) {
                    result[i][k++] = features[i][j];
                }
            }
        }
        return result;
    }
}
