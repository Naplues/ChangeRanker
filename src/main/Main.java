package main;

import nju.gzq.predictor.Predictor;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static double rate = 0.4;
    public static String rootPath = "C:\\Users\\gzq\\Desktop\\crash_data\\"; //C:\Users\GZQ\Desktop
    public static String testingPath = rootPath + "changeCandidate_" + rate + "\\";
    public static String trainingSinglePath = rootPath + "training_single\\";
    public static String trainingMultiplePath = rootPath + "training_" + rate + "\\";
    public static String resultPath = rootPath + "results\\";
    public static String classifier = "Logistic"; // Logistic  NB  MLP  J48  IBk  RF  SVM  PART
    public static String form = "Form2";
    public static String[] forms = {"Form1", "Form2", "Form3"};
    public static String[] versions = {"AspectJ", "JDT", "PDE", "Tomcat"};

    // "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"
    // "AspectJ", "JDT", "NetBeans", "PDE", "Tomcat"

    public static void main(String[] args) throws Exception {
        for (String form : forms) {
            System.out.println(form);
            // 测试ChangeLocator
            //System.out.println("ChangeLocator");
            //testChangeLocator(form, classifier, true);

            // 测试ChangeLocator + Wrapper
            //System.out.println("ChangeLocator + Wrapper");
            //testChangeLocatorWithFS(form, classifier, "Wrapper", true);

            // 测试ChangeLocator + CFS
            System.out.println("ChangeLocator + CFS");
            testChangeLocatorWithFS(form, classifier, "CFS", true);

            // 测试ChangeLocator + InfoGain
            //System.out.println("ChangeLocator + InfoGain");
            //testChangeLocatorWithFS(form, classifier, "InfoGain", true);

            // 测试ChangeRanker
            //testChangeRanker(Main.form, classifier, 3, true);
            //System.out.println("ChangeRanker");
            //testChangeRankerForNew(form, classifier, 3, true);
        }

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
            predict.predict(form, multiVersion, "ChangeLocator");
        }
    }

    /**
     * Test ChangeLocator + Feature Selection
     *
     * @param form
     * @param classifier
     * @throws Exception
     */
    public static void testChangeLocatorWithFS(String form, String classifier, String approach, boolean multiVersion) throws Exception {
        for (int i = 0; i < versions.length; i++) {
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, approach);
        }
    }


    /**
     * ChangeRanker
     *
     * @throws Exception
     */
    public static void testChangeRankerForNew(String form, String classifier, int deep, boolean multiVersion) throws Exception {

        for (int i = 0; i < versions.length; i++) {
            // 训练数据版本
            List<String> trainVersions = new ArrayList<>();
            trainVersions.add(versions[i]);
            // 在训练项目上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在本项目上测试性能, i: 本项目索引
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, "ChangeRanker", selectedFeatures);
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
        trainVersions.add("6.5");
        for (int i = 0; i < versions.length; i++) {
            // 在之前版本上选取特征子集
            Integer[] selectedFeatures = new MyRfsSelector().start(trainVersions, 10, deep, .0, 10);
            // 在下一版本上测试性能, i: 下一版本索引
            Predictor predict = new Predictor(versions[i], versions[i], form, classifier);
            predict.predict(form, multiVersion, "ChangeRanker", selectedFeatures);
            // 将下一版本i加入训练集
            trainVersions.add(versions[i]);
        }
    }
}
