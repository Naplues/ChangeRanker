package nju.gzq.other;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.util.List;

public class Test {

    public static void main(String[] args) {

        String[] projects = {"AspectJ", "JDT", "PDE", "Tomcat"}; //"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"
        String[] forms = {"Form1", "Form2", "Form3"};
        for (String project : projects) {
            for (String form : forms) {
                statistics(project, form);
            }
        }

    }

    public static void statistics(String project, String form) {
        String path = "C:\\Users\\GZQ\\Desktop\\data\\new_project\\changeCandidate\\" + form + "\\" + project + "\\";
        File[] files = new File(path).listFiles();
        System.out.println(project + " " + form);
        for (File file : files) {
            List<String> lines = FileHandler.readFileToLines(file.getPath());
            System.out.println(file.getName() + ", " + (lines.size() - 1));
        }
    }
}
