
package nju.gzq.ml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.Main;
import util.EvaluationMetric;
import util.FileToLines;
import util.Pair;
import util.WriteLinesToFile;

public class Predictor {
    public String preVersion; // 训练版本
    public String nextVersion; //测试版本
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
     * @param targetVersion 目标版本
     */
    public void loadFiles(String targetVersion) {
        if (this.loadedVersion == null || !this.loadedVersion.equals(targetVersion)) {
            this.loadedVersion = targetVersion;
            String filename = "crash_data/candidates/" + targetVersion + ".txt";
            List<String> lines = FileToLines.fileToLines(filename);
            inducingRevisions = new HashMap();
            potentialRevisions = new HashMap();
            Iterator iterator = lines.iterator();
            //逐行访问候选集
            while (iterator.hasNext()) {
                String line = (String) iterator.next();
                String[] split = line.split("\t");
                int bid = Integer.parseInt(split[0]);  //bucket ID
                inducingRevisions.put(bid, new HashSet()); //inducing crash change 索引
                potentialRevisions.put(bid, new HashSet()); //non-inducing crash change 索引
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
     * 构建训练集
     */
    public void constructTrainingData() {
        String head = "key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing";
        String trainingLoc = main.Main.prefix + "prediction_data" + File.separator + this.nextVersion + File.separator + this.form + File.separator + "training";
        System.out.println(trainingLoc);
        File file = new File(trainingLoc);
        if (!file.exists()) file.mkdir();

        List<String> lines = new ArrayList();
        lines.add(head);
        this.loadFiles(this.preVersion);
        String previous = main.Main.prefix + "prediction_data" + File.separator + this.preVersion + File.separator + this.form + File.separator + "features";
        HashMap<String, String> keyValues = new HashMap();
        HashSet<String> posInstance = new HashSet();
        HashSet<String> negInstance = new HashSet();
        Iterator var10 = inducingRevisions.keySet().iterator();

        int bid;
        while (var10.hasNext()) {
            bid = (Integer) var10.next();
            HashSet<String> inducings = (HashSet) inducingRevisions.get(bid);
            HashSet<String> potential = (HashSet) potentialRevisions.get(bid);
            List<String> features = FileToLines.fileToLines(previous + File.separator + bid + ".txt");

            for (int i = 1; i < features.size(); ++i) {
                String[] split = ((String) features.get(i)).split("\t");
                if (potential.contains(split[0])) {
                    String line = bid + "@" + split[0];
                    boolean allZero = true;

                    for (int j = 1; j < split.length; ++j) {
                        if (Double.parseDouble(split[j]) != 0.0D) allZero = false;
                        line = line + "," + split[j];
                    }

                    String key = bid + "@" + split[0];
                    if (inducings.contains(split[0])) {
                        if (!allZero) posInstance.add(key);
                        line = line + ",true";
                    } else {
                        line = line + ",false";
                        negInstance.add(key);
                    }
                    keyValues.put(key, line);
                }
            }
        }

        bid = posInstance.size();
        if (bid > negInstance.size()) bid = negInstance.size();

        List<String> indexes = new ArrayList();
        Iterator var22 = negInstance.iterator();

        while (var22.hasNext()) {
            String key = (String) var22.next();
            indexes.add(key);
        }

        negInstance.clear();
        int count = 0;
        Random rand = new Random();

        while (count < bid) {
            int index = Math.abs(rand.nextInt()) % bid;
            if (!negInstance.contains(indexes.get(index))) {
                ++count;
                negInstance.add(indexes.get(index));
            }
        }

        Iterator var26 = posInstance.iterator();

        String filename;
        while (var26.hasNext()) {
            filename = (String) var26.next();
            lines.add(keyValues.get(filename));
        }

        var26 = negInstance.iterator();

        while (var26.hasNext()) {
            filename = (String) var26.next();
            lines.add(keyValues.get(filename));
        }

        filename = trainingLoc + File.separator + "train.csv";
        WriteLinesToFile.writeLinesToFile(lines, filename);
    }

    /**
     * 构建测试集
     */
    public void constructTestingData() {
        String head = "key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing";
        String testingLoc = main.Main.prefix + "prediction_data" + File.separator + this.nextVersion + File.separator + this.form + File.separator + "testing";
        String featureLoc = main.Main.prefix + "prediction_data" + File.separator + this.nextVersion + File.separator + this.form + File.separator + "features";
        File file = new File(testingLoc);
        if (!file.exists()) file.mkdir();

        HashMap<String, String> keyValues = new HashMap();
        HashSet<String> posInstance = new HashSet();
        HashSet<String> negInstance = new HashSet();
        this.loadFiles(this.nextVersion);
        Iterator iterator = inducingRevisions.keySet().iterator();

        while (iterator.hasNext()) {
            int bid = (Integer) iterator.next();
            HashSet<String> inducings = inducingRevisions.get(bid);
            keyValues.clear();
            posInstance.clear();
            negInstance.clear();
            List<String> features = FileToLines.fileToLines(featureLoc + File.separator + bid + ".txt");

            for (int i = 1; i < features.size(); ++i) {
                String[] split = features.get(i).split("\t");
                String line = bid + "@" + split[0];
                String key = bid + "@" + split[0];

                for (int j = 1; j < split.length; ++j) line = line + "," + split[j];

                if (inducings.contains(split[0])) {
                    posInstance.add(key);
                    line = line + ",true";
                } else {
                    negInstance.add(key);
                    line = line + ",false";
                }
                keyValues.put(key, line);
            }

            List<String> lines = new ArrayList();
            lines.add(head);
            Iterator var19 = posInstance.iterator();

            String filename;
            while (var19.hasNext()) {
                filename = (String) var19.next();
                lines.add(keyValues.get(filename));
            }

            var19 = negInstance.iterator();

            while (var19.hasNext()) {
                filename = (String) var19.next();
                lines.add(keyValues.get(filename));
            }

            filename = testingLoc + File.separator + bid + ".csv";
            WriteLinesToFile.writeLinesToFile(lines, filename);
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
     * @throws Exception
     */
    public void predict() throws Exception {
        String trainFileName = "crash_data/training_single/" + preVersion + ".csv";
        //训练集文件 train.csv
        File trainFile = new File(trainFileName);
        this.loadFiles(this.nextVersion);
        //建立结果文件夹results/Logistic
        List<List<Integer>> ranks = new ArrayList();
        String resultFile = "crash_data/results";
        File file = new File(resultFile);
        if (!file.exists()) file.mkdir();
        resultFile = resultFile + File.separator + this.classifier;
        file = new File(resultFile);
        if (!file.exists()) file.mkdir();

        Iterator iterator = inducingRevisions.keySet().iterator();

        while (true) {
            ArrayList result;
            // 处理单个bucket
            while (iterator.hasNext()) {
                int bid = (Integer) iterator.next(); //bucket ID
                HashSet<String> inducing = inducingRevisions.get(bid);
                HashSet<String> potential = potentialRevisions.get(bid);
                if (!isHit(potential, inducing)) {
                    ranks.add(new ArrayList());
                } else {
                    String testFileName = "crash_data/Form3/" + nextVersion + "/" + bid + ".csv";
                    File testFile = new File(testFileName);
                    HashMap<String, Pair<Integer, Double>> predictLabel = LearnToRank.learnToRank(trainFile, testFile, this.classifier);
                    //HashMap<String, Pair<Integer, Double>> predictLabel = LearnToRank.learnToRankWithIndex(trainFile, testFile, this.classifier);
                    //HashMap<String, Pair<Integer, Double>> predictLabel = LearnToRank.learnToRankWithFeatureSelection(trainFile, testFile, this.classifier);
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
                    WriteLinesToFile.writeLinesToFile(saveLines, resultFile + File.separator + bid + ".txt");
                }
            }

            int N = 10;
            double[] results = new double[N + 2];
            double[] topN = EvaluationMetric.topN(ranks, N);
            double map = EvaluationMetric.MAP(ranks);
            double mrr = EvaluationMetric.MRR(ranks);
            results[0] = map;
            results[1] = mrr;
            String filename = resultFile + File.separator + this.classifier + ".txt";
            result = new ArrayList();
            System.out.println("MAP:\t" + map);
            result.add("MAP:\t" + map);
            System.out.println("MRR:\t" + mrr);
            result.add("MRR:\t" + mrr);

            for (int i = 0; i < N; ++i) {
                System.out.println("TOP@" + (i + 1) + "\t" + topN[i]);
                result.add("TOP@" + (i + 1) + "\t" + topN[i]);
            }

            WriteLinesToFile.writeLinesToFile(result, filename);
            return;
        }
    }
}
