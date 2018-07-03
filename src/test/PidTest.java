package test;

import util.Bucket;
import util.Evaluation;
import util.Ranking;
import util.Util;

/**
 * 测试特征效果
 */
public class PidTest {

    /**
     * 测试单个特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testSingleFeature(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int n = 0; n < 11; n++) {
            for (int i = 0; i < versions.length; i++) {
                Bucket bucket = new Bucket(versions[i], Util.form, false);
                bucket.setFeatures(Ranking.rankByFeature(bucket, n));
                buckets[i] = bucket;
            }
            Evaluation.evaluation(buckets, false);
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
            System.out.println("n=" + n);
            for (int m = 0; m < 11; m++) {
                for (int i = 0; i < versions.length; i++) {
                    Bucket bucket = new Bucket(versions[i], Util.form, false);
                    bucket.setFeatures(Ranking.rankByFeature(bucket, n, m));
                    buckets[i] = bucket;
                }
                Evaluation.evaluation(buckets, false);
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
            Bucket bucket = new Bucket(versions[i], Util.form, false);
            bucket.setFeatures(Ranking.rankByFeature(bucket, features));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets, true);
        System.out.println("================================================================================");
    }

    /**
     * 测试Pi在候选集个数较少的bucket上的性能
     *
     * @param versions
     * @param features
     */
    public static void testLowDataSet(String[] versions, int threshold, int... features) {
        Bucket[] buckets = new Bucket[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(versions[i], Util.form, true, threshold);
            bucket.setFeatures(Ranking.rankByFeature(bucket, features));
            buckets[i] = bucket;
            bucket.printBucketNames();
        }
        Evaluation.evaLowDataset(buckets, false);
        System.out.println("================================================================================");
    }
}
