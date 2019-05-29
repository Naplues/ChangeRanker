package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Test {

    public static String[] projects = {"AspectJ", "JDT", "Tomcat"};
    public static Map<String, Integer> initialTrainingSize = new HashMap<>();

    static {
        initialTrainingSize.put("AspectJ", 20);
        initialTrainingSize.put("JDT", 9);
        initialTrainingSize.put("Tomcat", 18);
    }

    public static String[] forms = {"Form1", "Form2", "Form3"};


    public static void main(String[] args) throws IOException {

        for (String project : projects) {
            for (String form : forms) {
                getInitialDataSet(project, form);
                underSampling(project, form);
            }
        }
    }

    public static void getInitialDataSet(String project, String form) throws IOException {
        String path = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\" + project + "_time_sequence.txt";
        String changePath = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\changeCandidate\\" + form + "\\" + project + "\\";

        String trainingPath = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\training\\" + form + "\\" + project + "\\";
        File trainingFile = new File(trainingPath);
        if (!trainingFile.exists()) trainingFile.mkdirs();
        String testingPath = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\testing\\" + form + "\\" + project + "\\";
        File testingFile = new File(testingPath);
        if (!testingFile.exists()) testingFile.mkdirs();

        Set<String> testingSet = new HashSet<>();

        List<String> lines = FileHandler.readFileToLines(path);
        for (int i = 1; i < lines.size(); i++) {
            String bucketName = lines.get(i).split("\t")[0] + ".csv";
            File candidateFile = new File(changePath + bucketName);
            if (i <= initialTrainingSize.get(project)) {
                Files.copy(candidateFile.toPath(), new File(trainingPath + bucketName).toPath());

            } else {
                Files.copy(candidateFile.toPath(), new File(testingPath + bucketName).toPath());
                testingSet.add(lines.get(i).split("\t")[0]);
            }
        }

        String candidatesPath = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\changeCandidate\\" + form + "\\candidates\\" + project + ".txt";
        String newCandidatesPath = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\testing\\" + form + "\\candidates\\";
        File newCandidatesFile = new File(newCandidatesPath);
        if (!newCandidatesFile.exists()) newCandidatesFile.mkdirs();
        Files.copy(new File(candidatesPath).toPath(), new File(newCandidatesPath + project + ".txt").toPath());

        StringBuilder text = new StringBuilder();
        List<String> lists = FileHandler.readFileToLines(newCandidatesPath + project + ".txt");
        for (String list : lists) {
            if (testingSet.contains(list.split("\t")[0])) text.append(list).append("\n");
        }
        FileHandler.writeStringToFile(newCandidatesPath + project + ".txt", text.toString());

    }

    public static void underSampling(String project, String form) {
        String path = "C:\\Users\\gzq\\Desktop\\data\\new_project\\training\\" + form + "\\" + project + "\\";
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
        String outPath = "C:\\Users\\gzq\\Desktop\\data\\new_project\\training\\" + form + "\\" + project + ".csv";
        FileHandler.writeStringToFile(outPath, text.toString());
    }

}
