package nju.gzq;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;

public class Learning {
    public static void main(String[] args) throws Exception {
        run();
    }


    /**
     * 获取训练数据
     *
     * @param version
     * @return
     */
    public static Instances getTrainData(String version) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setFile(new File("crash_data\\training\\" + version + ".csv"));
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        //instances.deleteAttributeAt(0);
        return instances;
    }

    /**
     * 获取测试数据
     *
     * @param version
     * @return
     */
    public static Instances getTestData(String version) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setFile(new File("crash_data\\Form3\\" + version + "\\75311.csv"));
        Instances instances = loader.getDataSet();
        instances.setClassIndex(instances.numAttributes() - 1);
        return instances;
    }


    public static void run() throws Exception {

        Instances trainData = getTrainData("6.5");
        Instances testData = getTestData("6.7");

        Classifier classifier = new Logistic();
        classifier.buildClassifier(trainData);

        Instance testInstance;
        Evaluation evaluation = new Evaluation(trainData);
        for (int i = 0; i < testData.numInstances(); i++) {
            testInstance = testData.instance(i);
            double[] value = classifier.distributionForInstance(testInstance);
            System.out.println(value[0] + "\t" + testInstance.classValue());
            //System.out.println(testInstance.toString(0) + "--" + testInstance.classValue() + "--" + value);
        }

        System.out.println(testData.size());

    }
}
