package nju.gzq.other;

import nju.gzq.utils.FileHandler;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 采样类
 */
public class Sampling {
    static String dataSet = "new_project";

    static String[] projects = {"AspectJ", "JDT", "PDE", "Tomcat"}; //"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"  "AspectJ", "JDT", "PDE", "Tomcat"
    static String[] forms = {"Form1", "Form2", "Form3"};

    public static void main(String[] args) throws Exception {

        for (String project : projects) {
            for (String form : forms) {
                SMOTESampling(project, form, 0.7);
            }
        }
    }


    public static void SMOTESampling(String project, String form, double rate) throws Exception {
        //合并数据集
        combine(project, form, rate);
        //采样数据集
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("tmp.csv"));
        Instances data = loader.getDataSet();
        data.setClassIndex(12);
        SMOTE convert = new SMOTE();
        String[] options = {"-S", "1", "-P", "100.0", "-K", "5"};
        convert.setOptions(options);
        convert.setInputFormat(data);

        Instances instances = Filter.useFilter(data, convert);
        CSVSaver saver = new CSVSaver();
        saver.setInstances(instances);
        saver.setFile(new File("C:\\Users\\gzq\\Desktop\\data\\training_" + rate + "\\" + form + "\\" + project + ".csv"));
        saver.writeBatch();
        //平衡采样
        balance(project, form, rate);
    }


    public static void SMOSampling(String project, String form, double rate) throws Exception {
        //合并数据集
        combine(project, form, rate);
        //采样数据集
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("tmp.csv"));
        Instances data = loader.getDataSet();
        data.setClassIndex(12);
        SMOTE convert = new SMOTE();
        String[] options = {"-S", "1", "-P", "100.0", "-K", "5"};
        convert.setOptions(options);
        convert.setInputFormat(data);

        //Bagging classifier

        Instances instances = Filter.useFilter(data, convert);
        CSVSaver saver = new CSVSaver();
        saver.setInstances(instances);
        saver.setFile(new File("C:\\Users\\gzq\\Desktop\\data\\training_" + rate + "\\" + form + "\\" + project + ".csv"));
        saver.writeBatch();
        //平衡采样
        balance(project, form, rate);
    }


    public static void combine(String project, String form, double rate) {
        // 合并数据集
        String path = "C:\\Users\\GZQ\\Desktop\\data\\" + dataSet + "\\training_" + rate + "\\" + form + "\\" + project + "\\";
        File[] files = new File(path).listFiles();
        StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            for (int i = 1; i < lines.size(); i++) {
                text.append(lines.get(i)).append("\n");
            }
        }
        System.out.println(text.toString());
        FileHandler.writeStringToFile("tmp.csv", text.toString());
    }

    public static void balance(String project, String form, double rate) {
        List<String> lines = FileHandler.readFileToLines("C:\\Users\\gzq\\Desktop\\data\\training_" + rate + "\\" + form + "\\" + project + ".csv");
        StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
        List<String> falseLines = new ArrayList<>();
        int trueCounter = 0, falseCounter = 0;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).endsWith("true")) {
                trueCounter++;
                text.append(lines.get(i)).append("\n");
            } else {
                falseCounter++;
                falseLines.add(lines.get(i));
            }
        }

        for (int i = 0; i < trueCounter; i++) {
            int index = (int) (Math.random() * falseLines.size());
            text.append(falseLines.get(index)).append("\n");
        }

        System.out.println(project + " " + form + " " + trueCounter + " " + falseCounter);
        System.out.println(text.toString());
        String outPath = "C:\\Users\\gzq\\Desktop\\data\\training_" + rate + "\\" + form + "\\" + project + ".csv";
        FileHandler.writeStringToFile(outPath, text.toString());
    }
}
