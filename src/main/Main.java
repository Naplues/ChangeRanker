package main;

import nju.gzq.predictor.Predictor;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static double rate = 1.0;
    public static String rootPath = "C:\\Users\\gzq\\Desktop\\data\\new_project\\"; // "C:\\Users\\gzq\\Desktop\\data\\new_project\\"
    public static String testingPath = rootPath + "testing\\";
    public static String trainingMultiplePath = rootPath + "training\\";
    public static String resultPath = rootPath + "results\\";
    public static String classifier = "Logistic"; // Logistic  NB  MLP  J48  IBk  RF  SVM  PART
    public static String[] classifiers = {"Logistic", "NB", "MLP", "J48", "IBk", "SVM", "PART"};
    public static String form = "Form1";
    public static String[] forms = {"Form1", "Form2", "Form3"};
    public static String[] versions = {"AspectJ", "JDT", "Tomcat"};

    // "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"
    // "AspectJ", "JDT", "PDE", "Tomcat"

    public static void main(String[] args) throws Exception {
        //  for (String form : forms) {
        //for (String classifier : classifiers) {
        System.out.println(classifier);
        // 测试ChangeLocator
        //System.out.println("ChangeLocator");
        //testChangeLocator(form, classifier);

        // 测试ChangeLocator + Wrapper
        //testChangeLocatorWithFS(form, classifier, "Wrapper");

        // 测试ChangeLocator + CFS
        //testChangeLocatorWithFS(form, classifier, "CFS");

        // 测试ChangeLocator + InfoGain
        //testChangeLocatorWithFS(form, classifier, "SVM");

        // 测试ChangeRanker
        testChangeRanker(form, classifier, 3);
        // }

    }

    /**
     * Test ChangeLocator
     *
     * @param form
     * @param classifier
     * @throws Exception
     */
    public static void testChangeLocator(String form, String classifier) throws Exception {
        System.out.println("ChangeLocator");
        for (int i = 0; i < versions.length; i++) {
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, "ChangeLocator");
        }
    }

    /**
     * Test ChangeLocator + Feature Selection
     *
     * @param form
     * @param classifier
     * @throws Exception
     */
    public static void testChangeLocatorWithFS(String form, String classifier, String approach) throws Exception {
        System.out.println("ChangeLocator + " + approach);
        for (int i = 0; i < versions.length; i++) {
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, approach);
        }
        System.out.println();
    }

    /**
     * Test ChangeRanker
     *
     * @throws Exception
     */
    public static void testChangeRanker(String form, String classifier, int deep) throws Exception {
        System.out.println("ChangeRanker");
        for (int i = 0; i < versions.length; i++) {
            // 训练数据版本
            List<String> trainVersions = new ArrayList<>();
            trainVersions.add(versions[i]);
            // 在训练项目上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在本项目上测试性能, i: 本项目索引
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, "ChangeRanker", selectedFeatures);
        }
        System.out.println();
    }

    /**
     * ChangeRanker
     *
     * @throws Exception
     */
    public static void testChangeRankerInNetBeans(String form, String classifier, int deep) throws Exception {
        // 训练数据版本
        List<String> trainVersions = new ArrayList<>();
        trainVersions.add("6.5"); //添加6.5 作为最初的训练集
        for (int i = 0; i < versions.length; i++) {
            // 在之前版本上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在下一版本上测试性能, i: 下一版本索引
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, "ChangeRanker", selectedFeatures);
            // 将下一版本i加入训练集
            trainVersions.add(versions[i]);
        }
    }
}
