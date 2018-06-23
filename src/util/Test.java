package util;

/**
 * 测试特征效果
 */
public class Test {

    /**
     * 测试单个特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testSingleFeature(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int n = 0; n < 11; n++) {
            for (int i = 0; i < versions.length; i++) {
                Bucket bucket = new Bucket(versions[i]);
                bucket.setFeatures(Ranking.rankByFeature(bucket, n));
                buckets[i] = bucket;
            }
            Evaluation.evaluation(buckets);
            System.out.println("n=" + n);
        }
    }

    /**
     * 测试两个组合特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testGroupFeature(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int n = 0; n < 11; n++) {
            for (int m = 0; m < 11; m++) {
                for (int i = 0; i < versions.length; i++) {
                    Bucket bucket = new Bucket(versions[i]);
                    bucket.setFeatures(Ranking.rankByFeature(bucket, n, m));
                    buckets[i] = bucket;
                }
                Evaluation.evaluation(buckets);
                System.out.println("n=" + n + " m=" + m);
            }
        }
    }

    /**
     * 测试Pi方法在各版本数据集上的性能以及平均性能
     *
     * @param versions
     */
    public static void testPi(String[] versions, int... features) {
        Bucket[] buckets = new Bucket[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(versions[i]);
            bucket.setFeatures(Ranking.rankByFeature(bucket, features));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets);
    }
}
