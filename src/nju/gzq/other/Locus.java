package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.util.*;

public class Locus {
    static String LocusPath = "C:\\Users\\GZQ\\Desktop\\data\\Locus\\";

    public static void main(String[] args) {
        String[] projects = {"AspectJ"};  //, "JDT", "PDE", "Tomcat"
        String[] forms = {"Form1"}; // , "Form2", "Form3"
        double rate = 0.4;
        for (String project : projects) {
            for (String form : forms) {
                getDataSet(project, form, rate);

            }
        }
    }

    public static void getDataSet(String project, String form, double rate) {
        String path = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\changeCandidate_" + rate + "\\" + form + "\\" + project + "\\";

        Set<String> testSet = new HashSet<>();
        File[] files = new File(path).listFiles();
        for (File file : files) testSet.add(file.getName().replace(".csv", ""));

        String testingFile = LocusPath + "testing_" + rate + "\\" + form + "\\" + project + "\\";
        List<String> lines = FileHandler.readFileToLines(LocusPath + project + ".txt");
        for (int j = 0; j < 1; j++) {
            String[] temp = lines.get(j).split("\t");
            String bucketID = temp[0];
            List<Rank> ranks = new ArrayList<>();
            for (int i = 1; i < temp.length; i++) ranks.add(new Rank(temp[i]));
            Collections.sort(ranks);
            for (Rank rank : ranks) {
                System.out.println(rank);
            }
            System.out.println(ranks.size());
        }
    }
}

class Rank implements Comparable {
    String id;
    Double score;

    public Rank(String string) {
        String[] temp = string.split(":");
        this.id = temp[0];
        this.score = Double.parseDouble(temp[1]);
    }

    public int compareTo(Object o) {
        Rank rank = (Rank) o;
        if (this.score < rank.score) return 1;
        else return -1;
    }

    public String toString() {
        return this.id + "\t" + this.score;
    }
}

