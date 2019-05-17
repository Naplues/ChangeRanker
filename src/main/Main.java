package main;

import nju.gzq.predictor.Predictor;
import test.PLCTest;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static String rootPath = "C:\\Users\\GZQ\\Desktop\\Git\\ChangeRanker\\crash_data\\"; //C:\Users\GZQ\Desktop
    public static String testingPath = rootPath + "changeCandidate\\";
    public static String trainingSinglePath = rootPath + "training_single\\";
    public static String trainingMultiplePath = rootPath + "training_multiple\\";
    public static String resultPath = rootPath + "results\\";
    public static String[] forms = {"Form1", "Form2", "Form3"};
    public static String classifier = "Logistic"; // Logistic  NB  MLP  J48  IBk  RF  SVM  PART
    public static String[] versions = {"AspectJ", "JDT", "PDE", "Tomcat"};
    // "6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2" "AspectJ", "JDT", "PDE", "Tomcat"


    public static void main(String[] args) throws Exception {
        // 测试FC
        //for (int i = 1; i < versions.length; i++) PLCTest.testFeatureCombination(forms[2], i, 4, 8, 9);
        //测试特征选择器
        //PLCTest.testSelector(forms[0]);

        // 测试ChangeLocator
        testChangeLocator(MyRfsSelector.form, classifier, true);

        // 测试ChangeLocator + Wrapper
        //testChangeLocatorWithWrapper(forms[0], classifier, true);

        // 测试ChangeRanker
        //testChangeRanker(forms[0], classifier, 3, true);
        testChangeRankerInNewData(MyRfsSelector.form, classifier, 3, true);

    }

    /**
     * 测试ChangeLocator
     *
     * @param form
     * @param classifier
     * @throws Exception
     */
    public static void testChangeLocator(String form, String classifier, boolean multiVersion) throws Exception {
        for (int i = 0; i < versions.length; i++) {
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, false);
        }
    }

    /**
     * 测试ChangeLocator
     *
     * @param form
     * @param classifier
     * @throws Exception
     */
    public static void testChangeLocatorWithWrapper(String form, String classifier, boolean multiVersion) throws Exception {
        for (int i = 0; i < versions.length; i++) {
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, true);
        }
    }

    /**
     * ChangeRanker
     *
     * @throws Exception
     */
    public static void testChangeRanker(String form, String classifier, int deep, boolean multiVersion) throws Exception {
        // 训练数据版本
        List<String> trainVersions = new ArrayList<>();
        trainVersions.add(versions[0]); //添加6.5 作为最初的训练集
        for (int i = 1; i < versions.length; i++) {
            // 在之前版本上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在下一版本上测试性能, i: 下一版本索引
            Predictor predict = new Predictor(versions[i - 1], versions[i], form, classifier);
            predict.predict(form, multiVersion, true, selectedFeatures);
            // 将下一版本i加入训练集
            trainVersions.add(versions[i]);
        }
    }

    /**
     * ChangeRanker
     *
     * @throws Exception
     */
    public static void testChangeRankerInNewData(String form, String classifier, int deep, boolean multiVersion) throws Exception {

        for (int i = 0; i < versions.length; i++) {
            // 训练数据版本
            List<String> trainVersions = new ArrayList<>();
            trainVersions.add(versions[i]);
            // 在之前版本上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在下一版本上测试性能, i: 下一版本索引
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, true, selectedFeatures);

        }
    }
}
