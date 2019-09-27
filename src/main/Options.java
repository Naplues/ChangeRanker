package main;

import nju.gzq.utils.FileHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Options {
    // parameter settings
    public static Map<String, List<String>> options;

    static {
        options = new HashMap<>();

        List<String> LogisticOptions = new ArrayList<>();
        List<String> NBOptions = new ArrayList<>();
        List<String> MLPOptions = new ArrayList<>();
        List<String> J48Options = new ArrayList<>();

        List<String> IBkOptions = new ArrayList<>();
        List<String> RFOptions = new ArrayList<>();
        List<String> SVMOptions = new ArrayList<>();
        List<String> PARTOptions = new ArrayList<>();

        //////////////////////////////////////////////////////////////////////////////////////////////////// Logistic OK
        LogisticOptions.add("-R 1.0E-8 -M -1");
        LogisticOptions.add("-R 1.0E-7 -M -1");
        LogisticOptions.add("-R 1.0E-6 -M -1");
        LogisticOptions.add("-R 1.0E-5 -M -1");
        LogisticOptions.add("-R 1.0E-4 -M -1");

        LogisticOptions.add("-R 1.0E-8 -M 50");
        LogisticOptions.add("-R 1.0E-8 -M 100");
        LogisticOptions.add("-R 1.0E-8 -M 200");
        LogisticOptions.add("-R 1.0E-8 -M 500");
        LogisticOptions.add("-R 1.0E-8 -M 1000");

        //////////////////////////////////////////////////////////////////////////////////////////////////////// NB 2 OK
        NBOptions.add("");
        NBOptions.add("-K");
        NBOptions.add("-D");

        ///////////////////////////////////////////////////////////////////////////////////////////////////////// MLP OK
        MLPOptions.add("");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.1 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.2 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.4 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.5 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.1 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.3 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.4 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.5 -N 500 -V 0 -S 1 -E 20 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 100 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 300 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 800 -V 0 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 1000 -V 0 -S 1 -E 20 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 5 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 10 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 15 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 20 -S 1 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 25 -S 1 -E 20 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 3 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 5 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 7 -E 20 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 9 -E 20 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 5 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 10 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 30 -C -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 50 -C -I -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -I -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -R -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I -R");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -I");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -R");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -I -R");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -I -D");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -R -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -C");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -I");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -R");
        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20 -D");

        MLPOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 1 -E 20");

        ///////////////////////////////////////////////////////////////////////////////////////////////////////// J48 OK
        J48Options.add("");
        J48Options.add("-C 0.25 -M 2 -A -Q 1");
        J48Options.add("-C 0.1 -M 2 -A -Q 1");
        J48Options.add("-C 0.2 -M 2 -A -Q 1");
        J48Options.add("-C 0.3 -M 2 -A -Q 1");
        J48Options.add("-C 0.4 -M 2 -A -Q 1");
        J48Options.add("-C 0.5 -M 2 -A -Q 1");

        J48Options.add("-M 1 -A -Q 1");
        J48Options.add("-M 3 -A -Q 1");
        J48Options.add("-M 4 -A -Q 1");
        J48Options.add("-M 5 -A -Q 1");

        J48Options.add("-M 2 -N 2 -A -R -Q 1");
        J48Options.add("-M 2 -N 4 -A -R -Q 1");
        J48Options.add("-M 2 -N 5 -A -R -Q 1");
        J48Options.add("-M 2 -N 10 -A -R -Q 1");

        J48Options.add("-M 2 -A -Q 3");
        J48Options.add("-M 2 -A -Q 5");
        J48Options.add("-M 2 -A -Q 7");
        J48Options.add("-M 2 -A -Q 9");

        J48Options.add("-M 2 -N 3 -Q 1 -R");
        J48Options.add("-M 2 -Q 1 -U");
        J48Options.add("-C 0.25 -M 2 -Q 1 -S");
        J48Options.add("-C 0.25 -M 2 -Q 1 -A");

        J48Options.add("-C 0.25 -M 2 -Q 1");

        ///////////////////////////////////////////////////////////////////////////////////////////////////////// IBk OK
        IBkOptions.add("");
        IBkOptions.add("-I -K 1 -E -W 0 -X");
        IBkOptions.add("-F -K 1 -E -W 0 -X");

        IBkOptions.add("-I -K 1 -E -W 1 -X");
        IBkOptions.add("-I -K 1 -E -W 2 -X");
        IBkOptions.add("-I -K 1 -E -W 3 -X");
        IBkOptions.add("-I -K 1 -E -W 4 -X");
        IBkOptions.add("-I -K 1 -E -W 5 -X");

        IBkOptions.add("-I -K 3 -E -W 0 -X");
        IBkOptions.add("-I -K 5 -E -W 0 -X");
        IBkOptions.add("-I -K 7 -E -W 0 -X");
        IBkOptions.add("-I -K 9 -E -W 0 -X");

        ////////////////////////////////////////////////////////////////////////////////////////////////////////// RF OK
        RFOptions.add("");
        RFOptions.add("-I 100 -K 0 -S 1 -depth 0");
        RFOptions.add("-I 50 -K 0 -S 1 -depth 0");
        RFOptions.add("-I 80 -K 0 -S 1 -depth 0");
        RFOptions.add("-I 200 -K 0 -S 1 -depth 0");
        RFOptions.add("-I 500 -K 0 -S 1 -depth 0");

        RFOptions.add("-I 100 -K 0 -S 1 -depth 1");
        RFOptions.add("-I 100 -K 0 -S 1 -depth 2");
        RFOptions.add("-I 100 -K 0 -S 1 -depth 3");
        RFOptions.add("-I 100 -K 0 -S 1 -depth 5");
        RFOptions.add("-I 100 -K 0 -S 1 -depth 10");

        RFOptions.add("-I 100 -K 0 -S 3 -depth 0");
        RFOptions.add("-I 100 -K 0 -S 5 -depth 0");
        RFOptions.add("-I 100 -K 0 -S 7 -depth 0");
        RFOptions.add("-I 100 -K 0 -S 9 -depth 0");

        RFOptions.add("-I 100 -K 1 -S 1 -depth 0");
        RFOptions.add("-I 100 -K 2 -S 1 -depth 0");
        RFOptions.add("-I 100 -K 3 -S 1 -depth 0");
        RFOptions.add("-I 100 -K 4 -S 1 -depth 0");

        ///////////////////////////////////////////////////////////////////////////////////////////////////////// SVM OK
        SVMOptions.add("");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 0.5 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 2 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 3 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 5 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");

        SVMOptions.add("-C 1 -N 1 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 2 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1 -M");

        SVMOptions.add("-C 1 -N 0 -L 1.0e-1 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-2 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-4 -P 1.0e-12 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-5 -P 1.0e-12 -V -1 -W 1 -M");

        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-10 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-11 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-14 -V -1 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-15 -V -1 -W 1 -M");

        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V 5 -W 1 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V 10 -W 1 -M");

        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 3 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 5 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 7 -M");
        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 9 -M");

        SVMOptions.add("-C 1 -N 0 -L 1.0e-3 -P 1.0e-12 -V -1 -W 1");

        //////////////////////////////////////////////////////////////////////////////////////////////////////// PART OK
        PARTOptions.add("");
        PARTOptions.add("-M 2 -N 3 -Q 1 -R -B -U");

        PARTOptions.add("-M 2 -N 3 -Q 1 -R -B");
        PARTOptions.add("-M 2 -N 3 -Q 1 -R -U");
        PARTOptions.add("-M 2 -Q 1 -B -U");

        PARTOptions.add("-M 2 -N 3 -Q 1 -R");
        PARTOptions.add("-C 0.25 -M 2 -Q 1 -B");
        PARTOptions.add("-C 0.25 -M 2 -Q 1 -U");

        PARTOptions.add("-C 0.25 -M 2 -Q 1");

        PARTOptions.add("-C 0.1 -M 2 -Q 1");
        PARTOptions.add("-C 0.2 -M 2 -Q 1");
        PARTOptions.add("-C 0.3 -M 2 -Q 1");
        PARTOptions.add("-C 0.4 -M 2 -Q 1");
        PARTOptions.add("-C 0.5 -M 2 -Q 1");

        PARTOptions.add("-M 1 -N 3 -Q 1 -R -B -U");
        PARTOptions.add("-M 3 -N 3 -Q 1 -R -B -U");
        PARTOptions.add("-M 4 -N 3 -Q 1 -R -B -U");
        PARTOptions.add("-M 5 -N 3 -Q 1 -R -B -U");

        PARTOptions.add("-M 2 -N 1 -Q 1 -R -B -U");
        PARTOptions.add("-M 2 -N 2 -Q 1 -R -B -U");
        PARTOptions.add("-M 2 -N 4 -Q 1 -R -B -U");
        PARTOptions.add("-M 2 -N 5 -Q 1 -R -B -U");

        PARTOptions.add("-M 2 -N 5 -Q 3 -R -B -U");
        PARTOptions.add("-M 2 -N 5 -Q 5 -R -B -U");
        PARTOptions.add("-M 2 -N 5 -Q 7 -R -B -U");
        PARTOptions.add("-M 2 -N 5 -Q 9 -R -B -U");

        // 填充每个分类器对应的配置
        options.put("Logistic", LogisticOptions);
        options.put("NB", NBOptions);
        options.put("MLP", MLPOptions);
        options.put("J48", J48Options);

        options.put("IBk", IBkOptions);
        options.put("RF", RFOptions);
        options.put("SVM", SVMOptions);
        options.put("PART", PARTOptions);
    }

    public static List<String> getOptions(String classifier) {
        return options.get(classifier);
    }


    public static void calculateResult(String filePath, String classifier) {
        List<String> line = FileHandler.readFileToLines(filePath + classifier + ".csv");
        int size = line.size() / 7;

        List<String> r1List = new ArrayList<>();
        List<String> r5List = new ArrayList<>();
        List<String> r10List = new ArrayList<>();
        List<String> mrrList = new ArrayList<>();
        List<String> mapList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double r1 = .0, r5 = .0, r10 = .0, mrr = .0, map = .0;
            for (int j = 1; j < 7; j++) {
                String[] values = line.get(i * 7 + j).split(",");
                r1 += Double.parseDouble(values[0]);
                r5 += Double.parseDouble(values[1]);
                r10 += Double.parseDouble(values[2]);
                mrr += Double.parseDouble(values[3]);
                map += Double.parseDouble(values[4]);
            }
            r1List.add(String.format("%.3f", r1 / 6));
            r5List.add(String.format("%.3f", r5 / 6));
            r10List.add(String.format("%.3f", r10 / 6));
            mrrList.add(String.format("%.3f", mrr / 6));
            mapList.add(String.format("%.3f", map / 6));
        }
        for (int i = 0; i < r1List.size(); i++) {
            System.out.println(r1List.get(i) + "," + r5List.get(i) + "," + r10List.get(i) + "," + mrrList.get(i) + "," + mapList.get(i));
        }

    }

    public static void main(String[] args) {
        String[] classifiers = {"Logistic", "NB", "MLP", "J48", "IBk", "RF", "SVM", "PART"};
        String path = "C:/Users/GZQ/Desktop/parameter/";
        for (String classifier : classifiers) {
            System.out.println(classifier);
            calculateResult(path, classifier);
        }
    }
}
