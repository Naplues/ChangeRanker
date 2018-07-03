package test;

import util.FileHandle;

import java.util.List;

/**
 * ChangeLocator 实验结果
 */
public class Test {
    public static double[] read(String filePath) {
        List<String> lines = FileHandle.readFileToLines(filePath);
        double[] values = new double[5];
        values[0] = Double.parseDouble(lines.get(2).split("\t")[1]);  //Recall@1
        values[1] = Double.parseDouble(lines.get(6).split("\t")[1]);  //Recall@5
        values[2] = Double.parseDouble(lines.get(11).split("\t")[1]); //Recall@10
        values[3] = Double.parseDouble(lines.get(0).split("\t")[1]);  //MAP
        values[4] = Double.parseDouble(lines.get(1).split("\t")[1]);  //MRR
        return values;
    }

    public static void print(double[][] values) {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                System.out.print(values[i][j] + ",");
            }
            System.out.println();
        }

    }

    public static void average(double[][] values) {
        double[] avg = new double[5];
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < values.length; i++) {
                avg[j] += values[i][j];
                //System.out.println(avg[j]);
            }
            avg[j] /= values.length;
            System.out.print(avg[j] + ",");
        }
        System.out.println();
    }

    public static void run(String form) {
        String filePath = "C:\\Users\\gzq\\Desktop\\" + form + "\\";
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};

        double[][] values = new double[versions.length][];
        for (int i = 0; i < versions.length; i++) {
            values[i] = read(filePath + versions[i] + "\\Logistic.txt");
        }
        //print(values);
        System.out.println(form);
        average(values);
        System.out.println("==============================================================");
    }

    public static void main(String[] args) {
        System.out.println("Recall@1\tRecall@5\tRecall@10\tMAP\tMRR");
        run("Form1");
        run("Form2");
        run("Form3");
    }
}
