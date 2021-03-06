
package nju.gzq.predictor;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import util.Pair;
import weka.attributeSelection.*;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.meta.Bagging;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.CSVLoader;

public class LearnToRank {
    public static String option = "";
    //0: key, 1:NFA, 2: Function, 3:RLOCC, 4: RLOAC, 5: RLODC, 6: IADCP, 7: ITDCR, 8: RF, 9: IBF
    //10: CC, 11: IADCL, 12: Label
    public static Map map = new HashMap();
    //0:NFA, 1:RLOCC, 2: RLOAC, 3: RLODC, 4: IADCP, 5: ITDCR, 6: RF, 7: IBF
    //8: CC, 9: IADCL, 10: Label

    static {
        // 去除0,2之后的特征与原始索引的映射
        map.put(0, 1);
        map.put(1, 3);
        map.put(2, 4);
        map.put(3, 5);
        map.put(4, 6);
        map.put(5, 7);
        map.put(6, 8);
        map.put(7, 9);
        map.put(8, 10);
        map.put(9, 11);
        map.put(10, 12);
    }

    public static boolean containedInArray(int index, int[] array) {
        for (int arr : array) if (arr == index) return true;
        return false;
    }

    /**
     * ChangeRanker
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRankWithRfs(File trainFile, File testFile, String classifierName, Integer... selectedFeatures) throws Exception {
        int[] selected = new int[selectedFeatures.length];
        for (int i = 0; i < selected.length; i++) selected[i] = (int) map.get(selectedFeatures[i]);

        int[] abandonIndex = new int[12 - selectedFeatures.length];
        for (int i = 0, j = 0; i < 12; i++) if (!containedInArray(i, selected)) abandonIndex[j++] = i;

        //abandonIndex = new int[]{0, 1, 2, 3, 4, 5, 7, 8, 9};
        Classifier classifier = getClassifier(classifierName);
        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        for (int i = abandonIndex.length - 1; i >= 0; i--) trainDataset.deleteAttributeAt(abandonIndex[i]);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        for (int i = abandonIndex.length - 1; i >= 0; i--) testDataset.deleteAttributeAt(abandonIndex[i]);
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

    /**
     * 获取制定的分类器
     *
     * @param classifierName
     * @return
     */
    public static Classifier getClassifier(String classifierName) throws Exception {
        Classifier classifier;

        switch (classifierName) {
            case "Logistic": // 2
                classifier = new Logistic();
                break;
            case "NB":  // 2
                classifier = new NaiveBayes();
                break;
            case "MLP": // 14
                classifier = new MultilayerPerceptron();
                break;
            case "J48": // 9
                classifier = new J48();
                break;
            case "IBk": // 8
                classifier = new IBk();
                break;
            case "RF": // 4
                classifier = new RandomForest();
                break;
            case "SVM": // 10
                classifier = new SMO();
                break;
            case "PART": // 7
                classifier = new PART();
                break;
            default:
                classifier = new Bagging();
        }
        classifier.setOptions(Utils.splitOptions(option));
        return classifier;
    }

    /**
     * ChangeLocator + Wrapper
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRankWithWrapper(File trainFile, File testFile, String classifierName) throws Exception {
        Classifier classifier = getClassifier(classifierName);
        String classifierClass = "weka.classifiers.bayes.NaiveBayes";
        /*if (classifierName.equals("Logistic")) {
            classifierClass = "weka.classifiers.functions.Logistic";
        } else if (classifierName.equals("NB")) {
            classifierClass = "weka.classifiers.bayes.NaiveBayes";
        }*/

        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        trainDataset.deleteAttributeAt(2);
        trainDataset.deleteAttributeAt(0);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        int index = indexOfMinority(trainDataset);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        testDataset.deleteAttributeAt(2);
        testDataset.deleteAttributeAt(0);

        ///////////////////////////////////////////////////////////
        // 进行特征选择
        AttributeSelection attributeSelection = new AttributeSelection(); //属性选择器
        WrapperSubsetEval wse = new WrapperSubsetEval(); //评估方式
        String[] evalOptions = new String[]{"-B", classifierClass, "-E", "AUPRC"};
        wse.setOptions(evalOptions);
        attributeSelection.setEvaluator(wse); //评估器
        attributeSelection.setSearch(new BestFirst()); //搜索策略: BestFirst
        attributeSelection.SelectAttributes(trainDataset);
        //选择好的属性索引

        int[] indices = attributeSelection.selectedAttributes();
        /*
        for (int idx : indices) System.out.print(idx + " ");
        System.out.println();*/

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
     * ChangeLocator + Info Gain
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRankWithSVM(File trainFile, File testFile, String classifierName) throws Exception {
        Classifier classifier = getClassifier(classifierName);

        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap<>();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        trainDataset.deleteAttributeAt(2);
        trainDataset.deleteAttributeAt(0);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        int index = indexOfMinority(trainDataset);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        testDataset.deleteAttributeAt(2);
        testDataset.deleteAttributeAt(0);

        ///////////////////////////////////////////////////////////
        // 进行特征选择
        AttributeSelection attributeSelection = new AttributeSelection(); //属性选择器
        SVMAttributeEval attributeEval = new SVMAttributeEval(); //信息增益评估
        attributeSelection.setEvaluator(attributeEval); //评估器
        attributeSelection.setSearch(new Ranker()); //搜索策略: Ranker
        attributeSelection.SelectAttributes(trainDataset);

        //选择好的属性索引
        int[] indices = attributeSelection.selectedAttributes();

        for (int idx : indices) System.out.print(idx + " ");
        System.out.println();

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
     * ChangeLocator + Info Gain
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRankWithCFS(File trainFile, File testFile, String classifierName) throws Exception {
        Classifier classifier = getClassifier(classifierName);

        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap<>();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        trainDataset.deleteAttributeAt(2);
        trainDataset.deleteAttributeAt(0);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        int index = indexOfMinority(trainDataset);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        testDataset.deleteAttributeAt(2);
        testDataset.deleteAttributeAt(0);

        ///////////////////////////////////////////////////////////
        // 进行特征选择
        AttributeSelection attributeSelection = new AttributeSelection(); //属性选择器
        CfsSubsetEval attributeEval = new CfsSubsetEval(); // Cfs评估
        String[] evalOptions = new String[]{"-P", "1", "-E", "1"};
        attributeEval.setOptions(evalOptions);
        attributeSelection.setEvaluator(attributeEval); //评估器
        attributeSelection.setSearch(new BestFirst()); //搜索策略: BestFirst
        attributeSelection.SelectAttributes(trainDataset);
        //选择好的属性索引
        int[] indices = attributeSelection.selectedAttributes();
/*
        for (int idx : indices) System.out.print(idx + " ");
        System.out.println();*/

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
     * ChangeLocator
     *
     * @param trainFile
     * @param testFile
     * @param classifierName
     * @return
     * @throws Exception
     */
    public static HashMap<String, Pair<Integer, Double>> learnToRank(File trainFile, File testFile, String classifierName) throws Exception {
        int[] abandonIndex = {0,}; //1, 2, 3, 4, 5, 7, 8, 9
        Classifier classifier = getClassifier(classifierName);
        HashMap<String, Pair<Integer, Double>> predictLabel = new HashMap();
        CSVLoader loader = new CSVLoader();
        loader.setSource(trainFile);
        Instances trainDataset = loader.getDataSet();
        //trainDataset.deleteAttributeAt(2);
        for (int i = abandonIndex.length - 1; i >= 0; i--) trainDataset.deleteAttributeAt(abandonIndex[i]);

        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        loader = new CSVLoader();
        loader.setSource(testFile);
        Instances testDataset = loader.getDataSet();
        Instances copyTestDataset = new Instances(testDataset);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);
        copyTestDataset.setClassIndex(copyTestDataset.numAttributes() - 1);
        //testDataset.deleteAttributeAt(2);
        for (int i = abandonIndex.length - 1; i >= 0; i--) testDataset.deleteAttributeAt(abandonIndex[i]);
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
}
