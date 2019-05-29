package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.util.*;

public class Locus {
    static String LocusPath = "C:\\Users\\GZQ\\Desktop\\data\\Locus\\";

    public static void main(String[] args) {
        String[] projects = {"AspectJ", "JDT", "Tomcat"};  //, "JDT", "Tomcat"
        String[] forms = {"Form1", "Form2", "Form3"}; // , "Form2", "Form3"

        for (String form : forms) {
            System.out.println(form);
            for (String project : projects) {
                //getResult(project, form); //获取预测结果 9, 10, 10
                evaluate(project, form); // 预测结果
            }
            System.out.println();
        }
    }

    public static void getResult(String project, String form) {
        //测试集中的bucket
        Map<String, Map<String, String>> candidatesMap = new HashMap<>();
        String path = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\testing\\" + form + "\\" + project + "\\";
        Set<String> testSet = new HashSet<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            String bucketID = file.getName().replace(".csv", "");
            testSet.add(bucketID);
            Map<String, String> candidate = new HashMap<>();
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            for (int i = 1; i < lines.size(); i++) {
                String[] temp = lines.get(i).split(",");
                candidate.put(temp[0].split("@")[1], temp[temp.length - 1]);
            }
            candidatesMap.put(bucketID, candidate);
        }

        // revision hash code
        Map<String, String> revision = new HashMap<>();
        String revisionPath = "C:\\Users\\gzq\\Desktop\\changeranker\\" + project + "\\prediction_data\\revisionInfo.txt";
        List<String> revLines = FileHandler.readFileToLines(revisionPath);
        for (int i = 1; i < revLines.size(); i++) {
            String rev = revLines.get(i).split("\t")[0];
            revision.put(rev.substring(0, 10), rev.substring(0, 12));
        }

        String testingFilePath = LocusPath + "testing\\" + form + "\\" + project + "\\";
        File testingFile = new File(testingFilePath);
        if (!testingFile.exists()) testingFile.mkdirs();
        List<String> lines = FileHandler.readFileToLines(LocusPath + project + ".txt");
        for (String line : lines) {
            String[] temp = line.split("\t");
            String bucketID = temp[0];
            if (!testSet.contains(bucketID)) continue;
            String fileName = bucketID + ".txt";
            List<Rank> ranks = new ArrayList<>();
            for (int i = 1; i < temp.length; i++) {
                Rank rank = new Rank(temp[i]);
                rank.id = revision.get(rank.id);
                if (candidatesMap.get(bucketID).containsKey(rank.id)) {
                    ranks.add(rank);
                }
            }
            Collections.sort(ranks);
            StringBuilder text = new StringBuilder();
            for (Rank rank : ranks) {
                if (candidatesMap.get(bucketID).containsKey(rank.id)) {
                    text.append(rank).append("\t").append(candidatesMap.get(bucketID).get(rank.id)).append("\n");
                }
            }
            FileHandler.writeStringToFile(testingFilePath + fileName, text.toString());
        }
    }

    public static void evaluate(String project, String form) {
        String testingFilePath = LocusPath + "testing\\" + form + "\\" + project + "\\";
        String result = Recall(testingFilePath, 1);
        result += "," + Recall(testingFilePath, 5);
        result += "," + Recall(testingFilePath, 10);
        result += "," + MRR(testingFilePath);
        result += "," + MAP(testingFilePath);
        System.out.println(result);
    }

    public static String Recall(String path, int k) {
        File[] files = new File(path).listFiles();
        int total = files.length;
        double recall = .0;
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            if (lines.size() == 0) continue;
            for (int i = 0; i < k && i < lines.size(); i++) {
                if (lines.get(i).endsWith("true")) {
                    recall++;
                    break;
                }
            }
        }
        return String.format("%.3f", recall / total);
    }

    public static String MRR(String path) {
        File[] files = new File(path).listFiles();
        int total = files.length;
        double rr = .0;
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            if (lines.size() == 0) continue;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).endsWith("true")) {
                    rr += 1.0 / (i + 1);
                    break;
                }
            }
        }
        return String.format("%.3f", rr / total);
    }

    public static String MAP(String path) {
        File[] files = new File(path).listFiles();
        int total = files.length;
        double ap = .0;
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            if (lines.size() == 0) continue;
            double n = 1.0;
            double p = .0;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).endsWith("true")) {
                    p += n / (i + 1);
                    n++;
                }
            }
            ap += p / n;
        }
        return String.format("%.3f", ap / total);
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

