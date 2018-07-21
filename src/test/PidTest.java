package test;

import nju.gzq.pid.Bucket;
import nju.gzq.pid.Evaluation;
import nju.gzq.pid.Ranking;


/**
 * 测试特征效果
 */
public class PidTest {
    public static String rootPath = "data_form3\\";
    // 项目版本
    public static String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"}; //, "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"

    /**
     * 测试单个特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testSingleFeature(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int n = 0; n < 10; n++) {
            for (int i = 0; i < versions.length; i++) {
                Bucket bucket = new Bucket(rootPath + versions[i], false);
                bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, n));
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

        for (int n = 0; n < 10; n++) {
            System.out.println("n=" + n);
            for (int m = 0; m < 10; m++) {
                for (int i = 0; i < versions.length; i++) {
                    Bucket bucket = new Bucket(rootPath + versions[i], false);
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
    public static double[] testPid(String[] versions, Integer... features) {
        Bucket[] buckets = new Bucket[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(rootPath + versions[i], false);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            buckets[i] = bucket;
        }
        return Evaluation.evaluation(buckets, false);

    }

    /**
     * 测试Pi在候选集个数较少的bucket上的性能
     *
     * @param versions
     * @param features
     */
    public static void testLowDataSet(String[] versions, int threshold, Integer... features) {
        Bucket[] buckets = new Bucket[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(rootPath + versions[i], true, threshold);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            buckets[i] = bucket;
            bucket.printBucketNames();
        }
        Evaluation.evaLowDataset(buckets, false);
        System.out.println("================================================================================");
    }


    public static void testMoreFeature(String[] versions) {
        //4: pos, 8: is Component, 9: distance
        //2: RLOAC, 5: ITDCR
        //更多特征
        PidTest.testPid(versions, 4, 9, 8);

        //除去一个
        PidTest.testPid(versions, 9, 8, 5);
        PidTest.testPid(versions, 9, 8, 2);

        PidTest.testPid(versions, 4, 9, 5);
        PidTest.testPid(versions, 4, 9, 2);

        PidTest.testPid(versions, 4, 8, 5);
        PidTest.testPid(versions, 4, 8, 2);

        //保留一个
        PidTest.testPid(versions, 2, 5, 4);
        PidTest.testPid(versions, 2, 5, 8);
        PidTest.testPid(versions, 2, 5, 9);

        //增加一个
        PidTest.testPid(versions, 4, 9, 8, 5);
        PidTest.testPid(versions, 4, 9, 8, 2);

        //增加两个
        PidTest.testPid(versions, 4, 9, 8, 2, 5);
    }
}
