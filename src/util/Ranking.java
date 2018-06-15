package util;

import java.util.Arrays;
import java.util.Comparator;

/**
 * util.Ranking: 对revisions按照某个特征值进行排序
 */
public class Ranking {

    /**
     * 仅根据位置进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByPos(Bucket bucket) {

        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);

        for (int i = 0; i < inducing.length; i++) {
            Arrays.sort(inducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getPos().compareTo(o1.getPos());
                }
            });
        }
        for (int i = 0; i < notInducing.length; i++) {
            Arrays.sort(notInducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getPos().compareTo(o1.getPos());
                }
            });
        }
        result = adjustPos(inducing, notInducing);
        return result;
    }

    /**
     * 仅根据位置进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByTime(Bucket bucket) {

        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);

        for (int i = 0; i < inducing.length; i++) {
            Arrays.sort(inducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getTime().compareTo(o1.getTime());
                }
            });
        }
        for (int i = 0; i < notInducing.length; i++) {
            Arrays.sort(notInducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getTime().compareTo(o1.getTime());
                }
            });
        }
        result = adjustTime(inducing, notInducing);
        return result;
    }

    /**
     * 根据位置+组件进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByPi(Bucket bucket) {

        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);

        for (int i = 0; i < inducing.length; i++) {
            Arrays.sort(inducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double a1 = o1.getPos() * o1.getIsComponent();
                    Double a2 = o2.getPos() * o2.getIsComponent();
                    return a2.compareTo(a1);
                }
            });
        }
        for (int i = 0; i < notInducing.length; i++) {
            Arrays.sort(notInducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    Double a1 = o1.getPos() * o1.getIsComponent();
                    Double a2 = o2.getPos() * o2.getIsComponent();
                    return a2.compareTo(a1);
                }
            });
        }
        result = adjustPi(inducing, notInducing);
        return result;
    }

    /**
     * 仅根据距离进行排序
     *
     * @param bucket
     */
    public static Feature[][] rankByDistance(Bucket bucket) {
        Feature[][] result = bucket.getFeatures();
        Feature[][] inducing = getInducing(result, true);
        Feature[][] notInducing = getInducing(result, false);

        for (int i = 0; i < inducing.length; i++) {
            Arrays.sort(inducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getDistance().compareTo(o1.getDistance());
                }
            });
        }
        for (int i = 0; i < notInducing.length; i++) {
            Arrays.sort(notInducing[i], new Comparator<Feature>() {
                @Override
                public int compare(Feature o1, Feature o2) {
                    return o2.getDistance().compareTo(o1.getDistance());
                }
            });
        }
        result = adjustDistance(inducing, notInducing);
        return result;
    }


    private static Feature[][] getInducing(Feature[][] features, boolean isInducing) {
        int[] count = new int[features.length];
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) if (features[i][j].isInducing() == isInducing) count[i]++;
        }

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
     * 调整位置 Pi
     *
     * @param inducing
     * @param notInducing
     * @return
     */
    private static Feature[][] adjustPi(Feature[][] inducing, Feature[][] notInducing) {
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
                        double ind = inducing[i][j].getPos() * inducing[i][j].getIsComponent();
                        double notInd = notInducing[i][k].getPos() * notInducing[i][k].getIsComponent();
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }

    /**
     * 调整位置 Distance
     *
     * @param inducing
     * @param notInducing
     * @return
     */
    private static Feature[][] adjustDistance(Feature[][] inducing, Feature[][] notInducing) {
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
                        double ind = inducing[i][j].getDistance();
                        double notInd = notInducing[i][k].getDistance();
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }
    /**
     * 调整位置 Distance
     *
     * @param inducing
     * @param notInducing
     * @return
     */
    private static Feature[][] adjustPos(Feature[][] inducing, Feature[][] notInducing) {
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
                        double ind = inducing[i][j].getPos();
                        double notInd = notInducing[i][k].getPos();
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }
    /**
     * 调整位置 Time
     *
     * @param inducing
     * @param notInducing
     * @return
     */
    private static Feature[][] adjustTime(Feature[][] inducing, Feature[][] notInducing) {
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
                        double ind = inducing[i][j].getTime();
                        double notInd = notInducing[i][k].getTime();
                        if (ind > notInd) result[i][m] = inducing[i][j++];
                        else result[i][m] = notInducing[i][k++];
                    }
                }
            }
        }
        return result;
    }
}
