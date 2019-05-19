package main;

import nju.gzq.selector.RfsSelector;
import nju.gzq.selector.fc.Evaluation;
import nju.gzq.selector.fc.Project;
import nju.gzq.selector.fc.Ranking;

import java.util.List;

public class MyRfsSelector extends RfsSelector {

    public static int labelIndex = 12;
    public static int[] abandonIndex = {0, 2};//, 3, 4, 5, 8, 9

    /**
     * 训练测试: 测试PLC方法在各版本数据集上的性能以及平均性能
     *
     * @param features
     */
    public static double[] trainFeatureCombination(String form, List<String> trainVersions, boolean details, Integer... features) {
        Project[] projects = new Project[trainVersions.size()];
        for (int i = 0; i < projects.length; i++) {
            Project bucket = new Project(Main.testingPath + "/" + form + "/" + trainVersions.get(i), labelIndex, abandonIndex);
            bucket.setFeatures(Ranking.rankByFeature(bucket, Ranking.MULTIPLE, Ranking.RANK_DESC, features));
            projects[i] = bucket;
        }
        return Evaluation.evaluation(projects, details);
    }

    @Override
    public double getValue(List<String> trainVersions, Integer[] features) {

        return trainFeatureCombination(Main.form, trainVersions, false, features)[3];
    }
}