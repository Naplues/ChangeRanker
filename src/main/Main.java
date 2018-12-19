package main;

import nju.gzq.ml.Predictor;
import test.PLCTest;

public class Main {

    public static String form = "Form3";
    public static String classifier = "Logistic";
    public static String prefix = "D://Documents/ChangeLoactor/";
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};


    public static void main(String[] args) throws Exception {
        // 测试FC
        //PLCTest.testFeatureCombination(0, 4, 8, 9);
        //测试特征选择器
        //PLCTest.testSelector();


        //测试ML
        for (int i = 0; i < versions.length - 1; i++) {
            Predictor predict = new Predictor(versions[i], versions[i + 1], form, classifier);
            //predict.constructTrainingData();
            //predict.constructTestingData();
            predict.predict(true, true);
        }
    }
}
