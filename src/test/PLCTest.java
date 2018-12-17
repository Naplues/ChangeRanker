package test;

import main.MySelector;
import nju.gzq.fc.Project;
import nju.gzq.fc.Evaluation;
import nju.gzq.fc.Ranking;

import java.util.ArrayList;
import java.util.List;


/**
 * 测试特征效果
 */
public class PLCTest {
    public static String rootPath = "crash_data\\Form3\\";  //buckets_data\low\10\
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
    public static int labelIndex = 12;
    public static int[] abandonIndex = {0, 2};//, 3, 4, 5, 8, 9

    /**
     * 训练测试: 测试PLC方法在各版本数据集上的性能以及平均性能
     *
     * @param features
     */
    public static double[] trainFeatureCombination(List<String> trainVersions, boolean details, Integer... features) {
        Project[] projects = new Project[trainVersions.size()];
        for (int i = 0; i < trainVersions.size(); i++) {
            Project bucket = new Project(rootPath + trainVersions.get(i), labelIndex, abandonIndex);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            projects[i] = bucket;
        }
        return Evaluation.evaluation(projects, details);
    }

    /**
     * 测试PLC在指定版本上的性能
     *
     * @param targetVersion
     * @param features
     * @return
     */
    public static double[] testFeatureCombination(int targetVersion, Integer... features) {
        Project project = new Project(rootPath + versions[targetVersion], labelIndex, abandonIndex);
        project.setFeatures(Ranking.rankByFeature(project, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
        return Evaluation.evaluation(project, true);
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
            Integer[] featureCombination = new MySelector().start(trainVersions, 10, 10, .0, 10);
            // 在下一版本上测试性能, i: 下一版本索引
            testFeatureCombination(i, featureCombination);
            // 将下一版本i加入训练集
            trainVersions.add(versions[i]);
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
            Project bucket = new Project(rootPath + versions[i], labelIndex, abandonIndex);
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
            PLCTest.trainFeatureCombination(feature[i]);
        }

        // 两个特征 10
        for (int i = 0; i < feature.length - 1; i++) {
            for (int j = i + 1; j < feature.length; j++) {
                System.out.print(i + "+" + j + ",  ");
                PLCTest.trainFeatureCombination(feature[i], feature[j]);
            }
        }

        // 三个特征 10
        for (int k = 0; k < feature.length - 2; k++) {
            for (int i = k + 1; i < feature.length - 1; i++) {
                for (int j = i + 1; j < feature.length; j++) {
                    System.out.print(k + "+" + i + "+" + j + ", ");
                    PLCTest.trainFeatureCombination(feature[i], feature[j], feature[k]);
                }
            }
        }

        //四个特征 5
        for (int m = 0; m < feature.length - 3; m++) {
            for (int k = m + 1; k < feature.length - 2; k++) {
                for (int i = k + 1; i < feature.length - 1; i++) {
                    for (int j = i + 1; j < feature.length - 0; j++) {
                        System.out.print(m + "+" + k + "+" + i + "+" + j + ", ");
                        PLCTest.trainFeatureCombination(feature[i], feature[j], feature[k], feature[m]);
                    }
                }
            }
        }

        //五个特征 1
        System.out.print("0+1+2+3+4,  ");
        PLCTest.trainFeatureCombination(feature);
    }*/
}
