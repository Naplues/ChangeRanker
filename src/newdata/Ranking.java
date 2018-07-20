package newdata;

import java.util.Arrays;
import java.util.Comparator;

import util.Bucket;

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
        rank(result, features);
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
                    Double a1 = .0;
                    Double a2 = .0;
                    for (int f : feature) {
                        a1 += o1.getValue(f);
                        a2 += o2.getValue(f);
                    }
                    o1.setTemp(a1);
                    o2.setTemp(a2);
                    return a2.compareTo(a1);
                }
            });
        }
    }
}
