package nju.gzq.other;

import nju.gzq.utils.FileHandle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Extractor {

    public static String originPath = "C:\\Users\\GZQ\\Desktop\\D_All\\prediction_data\\";
    public static String targetPath = "C:\\Users\\GZQ\\Desktop\\crash_data\\";

    public static void main(String[] args) throws IOException {
        String[] projects = {"AspectJ", "JDT", "PDE", "Tomcat"};
        String[] forms = {"Form1", "Form2", "Form3"};
        for (String project : projects) {
            for (String form : forms) {
                //changeCandidate(project, form);
                outTrain(project, form);
            }
        }
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
                List<String> lines = FileHandle.readFileToLines(originPath + p + "\\" + form + "\\training\\train.csv");
                for (int i = 1; i < lines.size(); i++) text.append(lines.get(i)).append("\n");
            }
            FileHandle.writeStringToFile(multiplePath + project + ".csv", text.toString());
        }

        //results
        File resultPath = new File(targetPath + "results\\");
        if (!resultPath.exists()) resultPath.mkdirs();
    }


    public static void outTrain(String project, String form) {
        String path = "C:\\Users\\GZQ\\Desktop\\crash_data\\changeCandidate\\" + form + "\\" + project + "\\";
        StringBuilder text = new StringBuilder("key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n");
        List<String> falseLines = new ArrayList<>();
        File[] files = new File(path).listFiles();
        int trueCounter = 0, falseCounter = 0;
        for (File file : files) {
            List<String> lines = FileHandle.readFileToLines(file.getPath());
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
            int index = (int) ( Math.random() * falseLines.size());
            text.append(falseLines.get(index)).append("\n");
        }

        System.out.println(project + " " + form + " " + trueCounter + " " + falseCounter);
        System.out.println(text.toString());
    }

}
