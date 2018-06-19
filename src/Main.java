import util.Bucket;
import util.Evaluation;
import util.Feature;
import util.Ranking;

public class Main {

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
        }
    }


    /**
     * 测试两个组合特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testCompFeature(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int n = 0; n < 11; n++) {
            for (int m = 0; m < n; m++) {
                for (int i = 0; i < versions.length; i++) {
                    Bucket bucket = new Bucket(versions[i]);
                    bucket.setFeatures(Ranking.rankByPi(bucket, n, m, 0));
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
    public static void testPi(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(versions[i]);
            bucket.setFeatures(Ranking.rankByPi(bucket, Feature.POS, Feature.ISCOMPONENT, 0));
            //bucket.setFeatures(Ranking.rankByFeature(bucket, Feature.RF));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets);
    }

    /**
     * 测试Pid方法在各版本数据集上的性能以及平均性能
     *
     * @param versions
     */
    public static void testPid(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];

        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(versions[i]);
            bucket.setFeatures(Ranking.rankByPid(bucket, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets);
    }


    public static void main(String[] args) {
        // 项目版本
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        // 测试单个属性
        //testSingleFeature(versions);
        // testCompFeature(versions);
        // 测试Pi方法
        testPi(versions);
        // 测试Pid方法
        testPid(versions);

    }
}
