package main;

import nju.gzq.predictor.Predictor;
import test.PLCTest;

public class Main {

    public static String rootPath = "crash_data/changeCandidate/";
    public static String prefix = "D://Documents/ChangeLoactor/";
    public static String[] forms = {"Form1", "Form2", "Form3"};
    public static String classifier = "MLP"; // Logistic  NB  MLP  J48  IBk  RF  SVM  PART
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};

    public static void main(String[] args) throws Exception {
        // 测试FC
        // for (int i = 1; i < versions.length; i++) PLCTest.testFeatureCombination(forms[2], i, 4, 8, 9);
        //测试特征选择器
        //PLCTest.testSelector(forms[2]);

        //测试ML
        for (int i = 0; i < versions.length - 1; i++) {
            Predictor predict = new Predictor(versions[i], versions[i + 1], forms[2], classifier);
            predict.predict(forms[2], true, true);
        }
    }
}
