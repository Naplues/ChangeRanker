
package nju.gzq.predictor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import main.Main;

import util.FileToLines;
import util.Pair;
import util.WriteLinesToFile;

public class Predictor {
    public String preVersion;  // 训练版本
    public String nextVersion; // 测试版本
    public String form; // Form
    public String classifier; //分类器名称
    private String loadedVersion; //加载的版本
    private static HashMap<Integer, HashSet<String>> inducingRevisions; //true
    private static HashMap<Integer, HashSet<String>> potentialRevisions; //false

    public Predictor(String preVersion, String nextVersion, String form, String classifier) {
        this.preVersion = preVersion;
        this.nextVersion = nextVersion;
        this.form = form;
        this.classifier = classifier;
    }

    /**
     * 加载指定版本的change candidates数据
     *
     * @param targetVersion 目标测试版本
     */
    public void loadFiles(String form, String targetVersion) {
        // System.out.println("load: " + targetVersion);
        if (this.loadedVersion == null || !this.loadedVersion.equals(targetVersion)) {
            this.loadedVersion = targetVersion;
            String filename = Main.testingPath + "/" + form + "/candidates/" + targetVersion + ".txt";
            List<String> lines = FileToLines.fileToLines(filename);
            inducingRevisions = new HashMap<>();
            potentialRevisions = new HashMap<>();
            Iterator iterator = lines.iterator();
            //逐行访问候选集
            while (iterator.hasNext()) {
                String line = (String) iterator.next();
                String[] split = line.split("\t");
                int bid = Integer.parseInt(split[0]);  //bucket ID
                inducingRevisions.put(bid, new HashSet<>());  //inducing crash change 索引
                potentialRevisions.put(bid, new HashSet<>()); //non-inducing crash change 索引
                // 填充crash-inducing changes 数据
                String inducing = split[1].substring(1, split[1].length() - 1);
                String[] split2 = inducing.split(","); //crash-inducing changes 数组
                for (int i = 0; i < split2.length; ++i) inducingRevisions.get(bid).add(split2[i].trim());
                // 填充non-crash inducing changes 数据
                String candidates = split[2].substring(1, split[2].length() - 1);
                split2 = candidates.split(",");
                for (int i = 0; i < split2.length; ++i) potentialRevisions.get(bid).add(split2[i].trim());
            }
        }
    }

    /**
     * inducing集合 和 potential集合 发生冲突: 判断是否存在
     *
     * @param potential
     * @param inducing
     * @return
     */
    private static boolean isHit(HashSet<String> potential, HashSet<String> inducing) {
        Iterator iterator = inducing.iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            if (potential.contains(s)) return true;
        }
        return false;
    }

    /**
     * 预测
     *
     * @param approach 选择特征
     * @throws Exception
     */
    public void predict(String form, String approach, Integer... selectedFeatures) throws Exception {
        //训练集文件 train.csv
        File trainFile = new File(Main.trainingMultiplePath + form + "\\" + preVersion + ".csv");
        this.loadFiles(form, this.nextVersion);
        //建立结果文件夹results/Logistic/Project
        List<List<Integer>> ranks = new ArrayList<>();
        String resultFilePath = Main.resultPath + File.separator + this.classifier + File.separator + nextVersion;
        File file = new File(resultFilePath);
        if (!file.exists()) file.mkdirs();

        Iterator iterator = inducingRevisions.keySet().iterator();

        ArrayList result;
        // 处理单个bucket
        while (iterator.hasNext()) {
            int bid = (Integer) iterator.next(); //bucket ID
            HashSet<String> inducing = inducingRevisions.get(bid);
            HashSet<String> potential = potentialRevisions.get(bid);
            if (!isHit(potential, inducing)) {
                ranks.add(new ArrayList());
            } else {
                String testFileName = Main.testingPath + form + "/" + nextVersion + "/" + bid + ".csv";
                File testFile = new File(testFileName);
                HashMap<String, Pair<Integer, Double>> predictLabel;
                //方法选择
                switch (approach) {
                    case "ChangeLocator":
                        predictLabel = LearnToRank.learnToRank(trainFile, testFile, this.classifier);
                        break;
                    case "Wrapper":
                        predictLabel = LearnToRank.learnToRankWithWrapper(trainFile, testFile, this.classifier);
                        break;
                    case "CFS":
                        predictLabel = LearnToRank.learnToRankWithCFS(trainFile, testFile, this.classifier);
                        break;
                    case "SVM":
                        predictLabel = LearnToRank.learnToRankWithSVM(trainFile, testFile, this.classifier);
                        break;
                    case "ChangeRanker":
                        predictLabel = LearnToRank.learnToRankWithRfs(trainFile, testFile, this.classifier, selectedFeatures);
                        break;
                    default:
                        predictLabel = new HashMap<>();
                        break;
                }

                result = new ArrayList();
                Iterator keyIterator = predictLabel.keySet().iterator();
                //处理单个change的结果
                while (keyIterator.hasNext()) {
                    String key = (String) keyIterator.next();
                    //将change ID 和预测值对应起来 <192894@8bc8d1cd23df, 0.8607162006854425>
                    result.add(new Pair(key, ((Pair) predictLabel.get(key)).getValue()));
                    //储存bucket的oracle change ID和oracle
                }
                //根据数值从小到大排序
                Collections.sort(result);
                List<Integer> rank = new ArrayList(); //inducing changes 的排名位置
                List<String> saveLines = new ArrayList();

                for (int i = 0; i < result.size(); ++i) {
                    int index = result.size() - i - 1;
                    // change ID
                    String change = ((String) ((Pair) result.get(index)).getKey()).split("@")[1];
                    if (inducing.contains(change)) rank.add(i);
                    saveLines.add(((Pair) result.get(index)).getKey() + "\t" + ((Pair) result.get(index)).getValue() + "\t" + inducing.contains(change));
                }
                ranks.add(rank);
                WriteLinesToFile.writeLinesToFile(saveLines, resultFilePath + File.separator + bid + ".txt");
            }
        }

        int N = 10;
        double[] results = new double[N + 2];
        double[] topN = EvaluationMetric.topN(ranks, N);
        double map = EvaluationMetric.MAP(ranks);
        double mrr = EvaluationMetric.MRR(ranks);
        results[0] = map;
        results[1] = mrr;
        System.out.println(String.format("%.3f", topN[0]) + ", " +
                String.format("%.3f", topN[4]) + ", " +
                String.format("%.3f", topN[9]) + ", " +
                String.format("%.3f", mrr) + ", " +
                String.format("%.3f", map));
    }
}
