package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.util.List;

public class Low {
    public static String filePath = "crash_data/lowCandidate/";
    public static String[] forms = {"Form1", "Form2", "Form3"};
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};

    /**
     * 保留某个版本的low数据集
     *
     * @param version
     * @param threshold
     */
    public static void remainLowCandidate(String form, String version, int threshold) {
        File[] files = new File(filePath + threshold + "/" + form + "/" + version + "/").listFiles();
        for (int i = 0; i < files.length; i++) {
            List<String> lines = FileHandler.readFileToLines(files[i].getPath());
            if (lines.size() - 1 <= threshold) new File(files[i].getPath()).delete();
        }
    }

    public static void remainLowCandidateFile(String form, String version, int threshold) {
        File[] buckets = new File(filePath + threshold + "/" + form + "/" + version + "/").listFiles();
        List<String> lines = FileHandler.readFileToLines("crash_data/changeCandidate/" + form + "/candidates/" + version + ".txt");
        String text = "";
        for (String line : lines) {
            for (int i = 0; i < buckets.length; i++) {
                if (buckets[i].getName().contains(line.split("\t")[0])) {
                    text += line + "\n";
                    break;
                }
            }
        }
        FileHandler.writeStringToFile(filePath + threshold + "/" + form + "/candidates/" + version + ".txt", text);
    }

    public static void main(String[] args) {

        for (String form : forms) {
            for (String version : versions) {
                remainLowCandidate(form, version, 10);
                remainLowCandidateFile(form, version, 10);
            }
        }
        System.out.println("Finish!");
    }
}
