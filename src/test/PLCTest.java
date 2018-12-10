package test;

import main.MySelector;
import nju.gzq.plc.Project;
import nju.gzq.plc.Evaluation;
import nju.gzq.plc.Ranking;

import java.util.ArrayList;
import java.util.List;


/**
 * 测试特征效果
 */
public class PLCTest {
    public static String rootPath = "crash_data\\Form3\\";  //buckets_data\low\10\
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};

    /**
     * 训练测试: 测试PLC方法在各版本数据集上的性能以及平均性能
     *
     * @param features
     */
    public static double[] testPLC(List<String> trainVersions, Integer... features) {
        Project[] buckets = new Project[trainVersions.size()];
        for (int i = 0; i < trainVersions.size(); i++) {
            Project bucket = new Project(rootPath + trainVersions.get(i), false);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            buckets[i] = bucket;
        }
        return Evaluation.evaluation(buckets, false);
    }

    /**
     * 测试PLC在制定版本上的性能
     *
     * @param targetVersion
     * @param features
     * @return
     */
    public static double[] testPLC(int targetVersion, Integer... features) {
        Project bucket = new Project(rootPath + versions[targetVersion], false);
        bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
        return Evaluation.evaluation(bucket, true);
    }


    /**
     * 测试特征选择器
     *
     * @throws Exception
     */
    public static void testSelector() {
        // 训练数据版本
        List<String> trainVersions = new ArrayList<>();
        trainVersions.add(versions[0]); //添加6.5 作为最初的训练集
        for (int i = 1; i < versions.length; i++) {
            // 在之前版本上获得最有组合
            Integer[] featureCombination = new MySelector().start(trainVersions, 5, 5, .0, 10);
            for (int feature : featureCombination) System.out.print(feature + " ");
            System.out.println();

            // 在下一版本上测试性能
            testPLC(i, featureCombination);
            // 加入新的训练集
            trainVersions.add(versions[i]);
        }
    }

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
     * 测试PLC在候选集个数较少的bucket上的性能
     *
     * @param threshold
     * @param features
     */
    public static void testLowDataSet(int threshold, Integer... features) {
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

/*
    public static void testMoreFeature() {
        //0: NAF 4: pos 5: ITDCR 8: is Component, 9: distance

        Integer[] feature = {0, 4, 5, 8, 9};
        // 一个特征 5
        for (int i = 0; i < feature.length; i++) {
            System.out.print(i + ",  ");
            PLCTest.testPLC(feature[i]);
        }

        // 两个特征 10
        for (int i = 0; i < feature.length - 1; i++) {
            for (int j = i + 1; j < feature.length; j++) {
                System.out.print(i + "+" + j + ",  ");
                PLCTest.testPLC(feature[i], feature[j]);
            }
        }

        // 三个特征 10
        for (int k = 0; k < feature.length - 2; k++) {
            for (int i = k + 1; i < feature.length - 1; i++) {
                for (int j = i + 1; j < feature.length; j++) {
                    System.out.print(k + "+" + i + "+" + j + ", ");
                    PLCTest.testPLC(feature[i], feature[j], feature[k]);
                }
            }
        }

        //四个特征 5
        for (int m = 0; m < feature.length - 3; m++) {
            for (int k = m + 1; k < feature.length - 2; k++) {
                for (int i = k + 1; i < feature.length - 1; i++) {
                    for (int j = i + 1; j < feature.length - 0; j++) {
                        System.out.print(m + "+" + k + "+" + i + "+" + j + ", ");
                        PLCTest.testPLC(feature[i], feature[j], feature[k], feature[m]);
                    }
                }
            }
        }

        //五个特征 1
        System.out.print("0+1+2+3+4,  ");
        PLCTest.testPLC(feature);
    }*/
}
