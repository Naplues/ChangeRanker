package nju.gzq.utils;
/**
 * File handle class
 *
 * @author naplues
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileHandle {
    /**
     * 读取文件返回一个字符串列表
     *
     * @param filePath
     * @return
     */
    public static List<String> readFileToLines(String filePath, boolean... args) {
        BufferedReader reader;
        if (args.length > 0 && args[0])
            reader = FileHandle.getExternalPath(filePath); // 读取文件系统路径
        else
            reader = FileHandle.getActualPath(filePath); // 默认读取实际路径
        List<String> lines = new ArrayList<>();
        try {
            String s;
            while ((s = reader.readLine()) != null) lines.add(s);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * 读取文件返回一个字符串集合
     *
     * @param filePath
     * @param args
     * @return
     */
    public static Set<String> readFileToSet(String filePath, boolean... args) {
        BufferedReader reader;
        if (args.length > 0 && args[0])
            reader = FileHandle.getExternalPath(filePath); // 读取文件系统路径
        else
            reader = FileHandle.getActualPath(filePath); // 默认读取实际路径
        Set<String> lines = new HashSet<>();
        try {
            String s;
            while ((s = reader.readLine()) != null) lines.add(s);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * 将字符串写入文件，false表示覆盖
     *
     * @param filePath
     * @param data
     */
    public static void writeStringToFile(String filePath, String data, boolean... a) {
        try {
            // true = append file
            File file = new File(filePath);
            if (!file.exists()) file.createNewFile();
            boolean append = false;
            if (a.length == 1) append = a[0];
            FileWriter fileWriter = new FileWriter(file, append);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(data);
            bufferWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取实际的文件路径
     *
     * @param path
     * @return
     */
    public static BufferedReader getActualPath(String path) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取参数文件
     *
     * @param path
     * @return
     */
    public static BufferedReader getExternalPath(String path) {
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
