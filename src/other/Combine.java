package other;

import nju.gzq.FileHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 将原始数据集组合成单个训练集
 */
public class Combine {

    public static String rootPath = "crash_data\\";
    public static String sourcePath = rootPath + "Form3\\";
    public static String targetPath = rootPath + "Combine3\\";
    public static String oracleBucketNamePath = rootPath + "oracleBuckets\\";
    public static String[] versions = {"6.5", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};

    /**
     * Program Entry
     *
     * @param args
     */
    public static void main(String[] args) {
        //合并训练集数据
        for (int i = 1; i <= 6; i++) combineMultipleVersions(i);
    }

    /**
     * 将一个版本的数据合并
     *
     * @param version
     * @return
     */
    public static List<String> combineOneVersion(String version) {
        Set<String> oracleSet = FileHandle.readFileToSet(oracleBucketNamePath + version);
        File[] files = new File(sourcePath + version).listFiles();
        List<String> lists = new ArrayList<>();
        for (File file : files) {
            if (!oracleSet.contains(file.getName())) continue;
            List<String> lines = FileHandle.readFileToLines(file.getPath());
            for (int lineIndex = 1; lineIndex < lines.size(); lineIndex++) {
                String[] temp = lines.get(lineIndex).split(",");
                boolean allZero = true;
                if (temp[temp.length - 1].equals("true")) {
                    for (int i = 1; i < temp.length - 1; i++) {
                        if (!(temp[i].equals("0") || temp[i].equals("0.0"))) {
                            allZero = false;
                            break;
                        }
                    }
                } else {
                    allZero = false;
                }
                if (!allZero) lists.add(lines.get(lineIndex));
            }
        }
        return lists;
    }

    /**
     * 将多个版本的数据合并
     *
     * @param versionNumber 版本数目
     */
    public static void combineMultipleVersions(int versionNumber) {

        String text = "key,files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n";
        for (int i = 0; i < versionNumber; i++) {
            List<String> lines = combineOneVersion(versions[i]);
            for (String line : lines) {
                text += line + "\n";
            }
        }
        FileHandle.writeStringToFile(targetPath + versions[versionNumber] + "_train.csv", text);
        System.out.println("The train set for version " + versions[versionNumber] + "has combined into " + targetPath + versions[versionNumber] + "_train.csv");
    }
}
