
package nju.gzq.ml;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import util.Pair;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class LearnToRank {
    public static int[] abandonIndex = {0, 1, 2, 3, 4, 5, 7, 8, 9};

    /**
     * 特征选择后学习并且排序
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRankWithFeatureSelection(File trainFile, File testFile, String classifierName) throws Exception {

        Classifier classifier = getClassifier(classifierName);
        String classifierClass = "";
        if (classifierName.equals("Logistic")) {
            classifierClass = "weka.classifiers.functions.Logistic";
        } else if (classifierName.equals("NaiveBayes")) {
            classifierClass = "weka.classifiers.bayes.NaiveBayes";
        } else if (classifierName.equals("BayesNet")) {
            classifierClass = "weka.classifiers.bayes.BayesNet";
        } else if (classifierName.equals("J48")) {
            classifierClass = "weka.classifiers.trees.J48";
        }

        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        trainDataset.deleteAttributeAt(0);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        int index = indexOfMinority(trainDataset);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        testDataset.deleteAttributeAt(0);

        ///////////////////////////////////////////////////////////
        // 进行特征选择
        AttributeSelection attributeSelection = new AttributeSelection(); //属性选择器
        WrapperSubsetEval wse = new WrapperSubsetEval(); //评估方式
        String[] evalOptions = new String[]{"-B", classifierClass, "-E", "auc"};
        wse.setOptions(evalOptions);
        attributeSelection.setEvaluator(wse); //评估器
        attributeSelection.setSearch(new BestFirst()); //搜索策略: BestFirst
        attributeSelection.SelectAttributes(trainDataset);
        //选择好的属性索引
        int[] indices = attributeSelection.selectedAttributes();

        int N = trainDataset.numAttributes();
        int current = 0;
        int deleteIndex = 0;

        for (int i = 0; i < N; ++i) {
            if (deleteIndex < indices.length && i >= indices[current]) {
                ++current;
                ++deleteIndex;
            } else {
                trainDataset.deleteAttributeAt(deleteIndex);
                testDataset.deleteAttributeAt(deleteIndex);
            }
        }

        ///////////////////////////////////////////////////////////
        //构建分类器
        classifier.buildClassifier(trainDataset);

        for (int i = 0; i < testDataset.numInstances(); ++i) {
            Instance instance = testDataset.instance(i);
            double[] values = classifier.distributionForInstance(instance);
            String key = copyTestDataset.instance(i).stringValue(0);
            int label = (int) copyTestDataset.instance(i).value(copyTestDataset.numAttributes() - 1);
            label = 1 - label;
            double suspicious = values[index];
            predictLabel.put(key, new Pair(label, suspicious));
        }

        return predictLabel;
    }

    /**
     * 学习并且排序
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRank(File trainFile, File testFile, String classifierName) throws Exception {

        Classifier classifier = getClassifier(classifierName);
        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        for (int i = abandonIndex.length-1; i >= 0; i--) trainDataset.deleteAttributeAt(abandonIndex[i]);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        for (int i = abandonIndex.length-1; i >= 0; i--) testDataset.deleteAttributeAt(abandonIndex[i]);
        // 构建分类器
        classifier.buildClassifier(trainDataset);

        for (int i = 0; i < testDataset.numInstances(); ++i) {
            Instance instance = testDataset.instance(i); //测试实例
            double[] values = classifier.distributionForInstance(instance);
            String key = copyTestDataset.instance(i).stringValue(0);
            int label = (int) copyTestDataset.instance(i).value(copyTestDataset.numAttributes() - 1);
            label = 1 - label;
            double suspicious = values[0];
            predictLabel.put(key, new Pair(label, suspicious));
        }
        return predictLabel;
    }


    public static HashMap<String, Pair<Integer, Double>> learnToRankWithIndex(File trainDatasetFile, File testDatasetFile, String classifierName, List<Integer> indexes, int N) throws Exception {

        Classifier classifier = getClassifier(classifierName);
        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainDatasetFile);
        Instances trainDataset = loader.getDataSet();
        trainDataset.deleteAttributeAt(0);
        int current = 0;
        int deleteIndex = 0;

        int index;
        for (index = 0; index < N; ++index) {
            if (deleteIndex < indexes.size() && index >= indexes.get(current)) {
                ++current;
                ++deleteIndex;
            } else {
                trainDataset.deleteAttributeAt(deleteIndex);
            }
        }

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        index = indexOfMinority(trainDataset);
        loader = new CSVLoader();
        loader.setSource(testDatasetFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        testDataset.deleteAttributeAt(0);
        current = 0;
        deleteIndex = 0;

        int i;
        for (i = 0; i < N; ++i) {
            if (deleteIndex < indexes.size() && i >= indexes.get(current)) {
                ++current;
                ++deleteIndex;
            } else {
                testDataset.deleteAttributeAt(deleteIndex);
            }
        }

        classifier.buildClassifier(trainDataset);

        for (i = 0; i < testDataset.numInstances(); ++i) {
            Instance instance = testDataset.instance(i);
            double[] values = classifier.distributionForInstance(instance);
            String key = copyTestDataset.instance(i).stringValue(0);
            int label = (int) copyTestDataset.instance(i).value(copyTestDataset.numAttributes() - 1);
            label = 1 - label;
            double suspicious = values[index];
            predictLabel.put(key, new Pair(label, suspicious));
        }

        return predictLabel;
    }

    /**
     * 少数索引
     *
     * @param dataset
     * @return
     */
    public static int indexOfMinority(Instances dataset) {
        int index = 0;
        HashMap<Integer, Integer> valueStatistics = new HashMap();

        int minorityNum;
        for (minorityNum = 0; minorityNum < dataset.numInstances(); ++minorityNum) {
            Instance instance = dataset.instance(minorityNum);
            int indexValue = (int) instance.classValue();
            if (valueStatistics.get(indexValue) == null) valueStatistics.put(indexValue, 0);

            valueStatistics.put(indexValue, valueStatistics.get(indexValue) + 1);
        }

        minorityNum = 2147483647;
        Iterator iterator = valueStatistics.keySet().iterator();

        while (iterator.hasNext()) {
            int indexOfValue = (Integer) iterator.next();
            if (valueStatistics.get(indexOfValue) < minorityNum) {
                index = indexOfValue;
                minorityNum = valueStatistics.get(indexOfValue);
            }
        }
        return index;
    }


    /**
     * 获取制定的分类器
     *
     * @param classifierName
     * @return
     */
    public static Classifier getClassifier(String classifierName) {
        Classifier classifier;

        switch (classifierName) {
            case "Logistic":
                classifier = new Logistic();
                break;
            case "NaiveBayes":
                classifier = new NaiveBayes();
                break;
            case "BayesNet":
                classifier = new BayesNet();
                break;
            case "J48":
                classifier = new J48();
                break;
            default:
                classifier = new Logistic();
        }
        return classifier;
    }
}
