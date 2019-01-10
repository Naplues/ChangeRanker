package test;

import main.Main;
import main.MySelector;
import nju.gzq.selector.fc.Evaluation;
import nju.gzq.selector.fc.Project;
import nju.gzq.selector.fc.Ranking;


import java.util.ArrayList;
import java.util.List;


/**
 * 测试特征效果
 */
public class PLCTest {

    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
    public static int labelIndex = 12;
    public static int[] abandonIndex = {0, 2, 3, 4, 5, 8, 9};//, 3, 4, 5, 8, 9

    /**
     * 训练测试: 测试PLC方法在各版本数据集上的性能以及平均性能
     *
     * @param features
     */
    public static double[] trainFeatureCombination(String form, List<String> trainVersions, boolean details, Integer... features) {
        Project[] projects = new Project[trainVersions.size()];
        for (int i = 0; i < trainVersions.size(); i++) {
            Project bucket = new Project(Main.rootPath + "/" + form + "/" + trainVersions.get(i), labelIndex, abandonIndex);
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
    public static double[] testFeatureCombination(String form, int targetVersion, Integer... features) {
        Project project = new Project(Main.rootPath + form + "/" + versions[targetVersion], labelIndex, abandonIndex);
        project.setFeatures(Ranking.rankByFeature(project, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
        return Evaluation.evaluation(project, true);
    }


    /**
     * 测试特征选择器
     *
     * @throws Exception
     */
    public static void testSelector(String form) {
        // 训练数据版本
        List<String> trainVersions = new ArrayList<>();
        trainVersions.add(versions[0]); //添加6.5 作为最初的训练集
        for (int i = 1; i < versions.length; i++) {
            // 在之前版本上获得最有组合
            Integer[] featureCombination = new MySelector().start(trainVersions, 5, 5, .0, 5);
            // 在下一版本上测试性能, i: 下一版本索引
            testFeatureCombination(form, i, featureCombination);
            // 将下一版本i加入训练集
            trainVersions.add(versions[i]);
        }
    }
}
