package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Extractor {

    public static String originPath = "C:\\Users\\GZQ\\Desktop\\D_All\\prediction_data\\";
    public static String targetPath = "C:\\Users\\GZQ\\Desktop\\crash_data\\";

    public static void main(String[] args) throws IOException {
        double rate = 1.0;
        String[] projects = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"}; //"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"  "AspectJ", "JDT", "PDE", "Tomcat"
        String[] forms = {"Form1", "Form2", "Form3"};
        for (String project : projects) {
            for (String form : forms) {
                //changeCandidate(project, form);
                //extractExpData(project, form, rate);
                //underSampling(project, form, rate);
                overSampling(project, form, rate);
                //filterTestingBucket(project, form, rate);
                //test(project, form);
            }
        }
    }


    public static void overSampling(String project, String form, double rate) {
        String path = "C:\\Users\\gzq\\Desktop\\data\\netbeans_project\\training_" + rate + "\\" + form + "\\" + project + "\\";
        StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
        List<String> trueLines = new ArrayList<>();
        File[] files = new File(path).listFiles();
        int trueCounter = 0, falseCounter = 0;
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).endsWith("true")) {
                    trueCounter++;
                    trueLines.add(lines.get(i));
                } else {
                    falseCounter++;
                    text.append(lines.get(i)).append("\n");
                }
            }
        }

        for (int i = 0; i < falseCounter; i++) {
            text.append(trueLines.get(i % trueCounter)).append("\n");
        }

        System.out.println(project + " " + form + " " + trueCounter + " " + falseCounter);
        System.out.println(text.toString());
        String outPath = "C:\\Users\\gzq\\Desktop\\data\\over_sampling\\training_" + rate + "\\" + form + "\\";
        File outFolder = new File(outPath);
        if (!outFolder.exists()) outFolder.mkdirs();
        FileHandler.writeStringToFile(outPath + project + ".csv", text.toString());
    }

    public static void test(String project, String form) {
        String path = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\changeCandidate\\" + form + "\\" + project + "\\";
        int oracleCount = 0, count5 = 0, count10 = 0, total = 0;
        for (File file : new File(path).listFiles()) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            boolean hasOracle = false;
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).contains("true")) {
                    oracleCount++;
                    hasOracle = true;
                    break;
                }
            }
            if (hasOracle && lines.size() <= 5 + 1) count5++;
            if (hasOracle && lines.size() <= 10 + 1) count10++;
            total += lines.size() - 1;
        }
        System.out.println(project + ": " + form + " " + oracleCount + " " + count5 + " " + count10 + " " + total);
    }

    public static void changeCandidate(String project, String form) throws IOException {
        String path = originPath + project + "\\" + form + "\\";
        String tPath = targetPath + "changeCandidate\\" + form + "\\" + project + "\\";
        File tFile = new File(tPath);
        if (!tFile.exists()) tFile.mkdirs();
        File[] testingFiles = new File(path + "testing\\").listFiles();
        for (File testingFile : testingFiles)
            Files.copy(testingFile.toPath(), new File(tFile.getPath() + "\\" + testingFile.getName()).toPath());

        String candidatesPath = targetPath + "changeCandidate\\" + form + "\\candidates\\";
        File candidatesFile = new File(candidatesPath);
        if (!candidatesFile.exists()) candidatesFile.mkdirs();
        Files.copy(new File(path + "candidates.txt").toPath(), new File(candidatesPath + project + ".txt").toPath());

        //training single
        String singlePath = targetPath + "training_single\\";
        File singleFile = new File(singlePath);
        if (!singleFile.exists()) singleFile.mkdirs();
        if (form.equals("Form1"))
            Files.copy(new File(path + "training\\train.csv").toPath(), new File(singlePath + project + ".csv").toPath());

        //training multiple
        String multiplePath = targetPath + "training_multiple\\";
        File multipleFile = new File(multiplePath);
        if (!multipleFile.exists()) multipleFile.mkdirs();
        Set<String> set = new HashSet<>();
        set.add("AspectJ");
        set.add("JDT");
        set.add("PDE");
        set.add("Tomcat");
        set.remove(project);
        if (form.equals("Form1")) {
            StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
            for (String p : set) {
                List<String> lines = FileHandler.readFileToLines(originPath + p + "\\" + form + "\\training\\train.csv");
                for (int i = 1; i < lines.size(); i++) text.append(lines.get(i)).append("\n");
            }
            FileHandler.writeStringToFile(multiplePath + project + ".csv", text.toString());
        }

        //results
        File resultPath = new File(targetPath + "results\\");
        if (!resultPath.exists()) resultPath.mkdirs();
    }

    public static void extractExpData(String project, String form, double rate) throws IOException {
        String path = "C:\\Users\\gzq\\Desktop\\crash_data\\changeCandidate\\" + form + "\\" + project + "\\";
        String newTestPath = "C:\\Users\\gzq\\Desktop\\crash_data\\changeCandidate_" + rate + "\\" + form + "\\" + project + "\\";
        String newTrainPath = "C:\\Users\\gzq\\Desktop\\crash_data\\training_" + rate + "\\" + form + "\\" + project + "\\";
        String newCandidatePath = "C:\\Users\\gzq\\Desktop\\crash_data\\changeCandidate_" + rate + "\\" + form + "\\candidates\\";
        File newTrainFolder = new File(newTestPath);
        if (!newTrainFolder.exists()) newTrainFolder.mkdirs();
        File newTestFolder = new File(newTrainPath);
        if (!newTestFolder.exists()) newTestFolder.mkdirs();
        File newCandidateFolder = new File(newCandidatePath);
        if (!newCandidateFolder.exists()) newCandidateFolder.mkdirs();

        //复制buckets文件
        File[] buckets = new File(path).listFiles();
        double trainSize = buckets.length * rate;
        Set<Integer> bucketIDSet = new TreeSet<>();
        for (File bucket : buckets) bucketIDSet.add(Integer.parseInt(bucket.getName().replace(".csv", "")));
        int i = 0;
        for (Integer bucketID : bucketIDSet) {
            File sourceFile = new File(path + bucketID + ".csv");
            File targetFile;
            if (i < trainSize) {
                // 将训练集文件复制到training_rate文件夹
                targetFile = new File(newTrainPath + bucketID + ".csv");
                System.out.println(bucketID);
            } else {
                targetFile = new File(newTestPath + bucketID + ".csv");
            }
            Files.copy(sourceFile.toPath(), targetFile.toPath());
            i++;
        }
        // 复制candidates文件
        String candidatePath = "C:\\Users\\gzq\\Desktop\\crash_data\\changeCandidate\\" + form + "\\candidates\\" + project + ".txt";
        Files.copy(new File(candidatePath).toPath(), new File(newCandidatePath + project + ".txt").toPath());

    }

    public static void underSampling(String project, String form, double rate) {
        String path = "C:\\Users\\gzq\\Desktop\\crash_data\\training_" + rate + "\\" + form + "\\" + project + "\\";
        StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
        List<String> falseLines = new ArrayList<>();
        File[] files = new File(path).listFiles();
        int trueCounter = 0, falseCounter = 0;
        for (File file : files) {
            // System.out.println(file.getPath());
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).endsWith("true")) {
                    trueCounter++;
                    text.append(lines.get(i)).append("\n");
                } else {
                    falseCounter++;
                    falseLines.add(lines.get(i));
                }
            }
        }

        for (int i = 0; i < trueCounter; i++) {
            int index = (int) (Math.random() * falseLines.size());
            text.append(falseLines.get(index)).append("\n");
        }

        System.out.println(project + " " + form + " " + trueCounter + " " + falseCounter);
        System.out.println(text.toString());
        String outPath = "C:\\Users\\gzq\\Desktop\\crash_data\\training_" + rate + "\\" + form + "\\" + project + ".csv";
        FileHandler.writeStringToFile(outPath, text.toString());
    }

    public static void filterTestingBucket(String project, String form, double rate) {
        String basePath = "C:\\Users\\GZQ\\Desktop\\crash_data\\changeCandidate_" + rate + "\\" + form + "\\";
        String bucketPath = basePath + project + "\\";
        File[] buckets = new File(bucketPath).listFiles();
        Set<String> bucketID = new HashSet<>();
        for (File bucket : buckets) {
            bucketID.add(bucket.getName().replace(".csv", ""));
        }
        StringBuilder text = new StringBuilder();
        String candidatePath = basePath + "candidates\\" + project + ".txt";
        List<String> lines = FileHandler.readFileToLines(candidatePath);
        for (String line : lines) {
            if (bucketID.contains(line.split("\t")[0])) text.append(line).append("\n");
        }
        String outPath = basePath + "candidates\\" + project + ".txt";
        FileHandler.writeStringToFile(outPath, text.toString());
    }
}
