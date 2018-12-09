package test;

import nju.gzq.plc.Project;
import nju.gzq.plc.Evaluation;
import nju.gzq.plc.Ranking;


/**
 * 测试特征效果
 */
public class PLCTest {
    public static String rootPath = "buckets_data\\form3x5\\";  //buckets_data\low\10\
    // 项目版本
    public static String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"}; //, "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"

    /**
     * 测试单个特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testSingleFeature(String[] versions) {
        Project[] buckets = new Project[versions.length];

        for (int n = 0; n < 10; n++) {
            for (int i = 0; i < versions.length; i++) {
                Project bucket = new Project(rootPath + versions[i], false);
                bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, n));
                buckets[i] = bucket;
            }
            Evaluation.evaluation(buckets, true);
            //System.out.println("n=" + n);
        }
    }

    /**
     * 测试两个组合特征在各版本数据上的性能
     *
     * @param versions
     */
    public static void testGroupFeature(String[] versions) {
        Project[] buckets = new Project[versions.length];

        for (int n = 0; n < 10; n++) {
            System.out.println("n=" + n);
            for (int m = 0; m < 10; m++) {
                for (int i = 0; i < versions.length; i++) {
                    Project bucket = new Project(rootPath + versions[i], false);
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
        Project[] buckets = new Project[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Project bucket = new Project(rootPath + versions[i], false);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            buckets[i] = bucket;
        }
        return Evaluation.evaluation(buckets, true);

    }

    /**
     * 测试Pi在候选集个数较少的bucket上的性能
     *
     * @param versions
     * @param features
     */
    public static void testLowDataSet(String[] versions, int threshold, Integer... features) {
        Project[] buckets = new Project[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Project bucket = new Project(rootPath + versions[i], true, threshold);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            buckets[i] = bucket;
            bucket.printBucketNames();
        }
        Evaluation.evaLowDataset(buckets, false);
        System.out.println("================================================================================");
    }


    public static void testMoreFeature(String[] versions) {
        //0: NAF 4: pos 5: ITDCR 8: is Component, 9: distance

        //更多特征

        Integer[] feature = {0, 4, 5, 8, 9};
        // 一个特征 5
        for (int i = 0; i < feature.length; i++) {
            System.out.print(i + ",  ");
            PLCTest.testPid(versions, feature[i]);
        }

        // 两个特征 10
        for (int i = 0; i < feature.length - 1; i++) {
            for (int j = i + 1; j < feature.length; j++) {
                System.out.print(i + "+" + j + ",  ");
                PLCTest.testPid(versions, feature[i], feature[j]);
            }
        }


        // 三个特征 10
        for (int k = 0; k < feature.length - 2; k++) {
            for (int i = k + 1; i < feature.length - 1; i++) {
                for (int j = i + 1; j < feature.length; j++) {
                    System.out.print(k + "+" + i + "+" + j + ", ");
                    PLCTest.testPid(versions, feature[i], feature[j], feature[k]);
                }
            }
        }


        //四个特征 5
        for (int m = 0; m < feature.length - 3; m++) {
            for (int k = m + 1; k < feature.length - 2; k++) {
                for (int i = k + 1; i < feature.length - 1; i++) {
                    for (int j = i + 1; j < feature.length - 0; j++) {
                        System.out.print(m + "+" + k + "+" + i + "+" + j + ", ");
                        PLCTest.testPid(versions, feature[i], feature[j], feature[k], feature[m]);
                    }
                }
            }
        }


        //五个特征 1
        System.out.print("0+1+2+3+4,  ");
        PLCTest.testPid(versions, feature);

    }
}
