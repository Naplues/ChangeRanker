package nju.gzq.selector.fc;

import nju.gzq.utils.FileHandler;

import java.io.File;
import java.util.List;

/**
 * feature[][]: 每一行代表一个bucket, 每一个元素代表一个bucket中的一个revision的特征记录
 */
public class Project {
    private Feature[][] features;
    public static String[] featureNames;
    private String versionName;
    private String[] bucketNames;
    private int revisionNumber; //原来版本数目
    private int filterNumber;   //过滤后的版本数目

    public Feature[][] getFeatures() {
        return features;
    }

    /**
     * PLC: 保存一个项目的所有bucket信息
     *
     * @param path         项目版本号
     * @param labelIndex   标记值索引
     * @param abandonIndex 舍弃特征索引
     */
    public Project(String path, int labelIndex, int... abandonIndex) {
        File[] revisions = new File(path).listFiles();
        versionName = new File(path).getName();
        revisionNumber = revisions.length;
        bucketNames = new String[revisions.length];
        features = new Feature[revisions.length][];
        for (int i = 0; i < features.length; i++) {
            List<String> lines = FileHandler.readFileToLines(revisions[i].getPath());
            bucketNames[i] = revisions[i].getName();
            features[i] = new Feature[lines.size() - 1];
            for (int j = 1; j < lines.size(); j++) {
                features[i][j - 1] = new Feature(lines.get(j).split(","), labelIndex, abandonIndex);
            }
            // 设置特征名称与索引
            if (featureNames == null)
                setFeatureNames(lines.get(0).split(","), labelIndex, abandonIndex);
        }

        selectOracleFeature();  //选择有oracle的bucket

        String text = "";
        for (String name : bucketNames) text += name + "\n";
        // FileHandler.writeStringToFile("crash_data\\oracleBuckets\\" + versionName, text);
    }

    /**
     * 选择有oracle的bucket
     */
    public void selectOracleFeature() {
        int[] pass = new int[features.length];
        int count = 0;
        for (int i = 0, j; i < features.length; i++) {
            if (features[i].length == 1) continue;
            for (j = 0; j < features[i].length; j++)
                if (features[i][j].isLabel()) break;

            if (j == features[i].length) pass[i] = 1;
        }
        // 统计保留多少bucket
        int remain = 0;
        for (int i = 0; i < pass.length; i++) if (pass[i] == remain) count++;

        Feature[][] filterFeature = new Feature[count][];
        String[] filterBucketNames = new String[count];
        for (int i = 0, j = 0; i < features.length; i++) {
            if (pass[i] == remain) {
                filterFeature[j] = features[i];
                filterBucketNames[j] = bucketNames[i];
                j++;
            }
        }

        features = filterFeature;
        bucketNames = filterBucketNames;
        filterNumber = count;
    }

    /**
     * 选择候选集个数在指定阈值以内的bucket
     *
     * @param threshold 候选集数量阈值
     */
    public void selectLowFeature(int threshold) {
        int[] pass = new int[features.length];
        int count = 0;
        for (int i = 0; i < features.length; i++) if (features[i].length <= threshold) pass[i] = 1;

        // 统计保留多少bucket
        int remain = 1;
        for (int i = 0; i < pass.length; i++) if (pass[i] == remain) count++;

        Feature[][] filterFeature = new Feature[count][];
        String[] filterBucketNames = new String[count];
        for (int i = 0, j = 0; i < features.length; i++) {
            if (pass[i] == remain) {
                filterFeature[j] = features[i];
                filterBucketNames[j] = bucketNames[i];
                j++;
            }
        }
        features = filterFeature;
        bucketNames = filterBucketNames;
        filterNumber = count;
    }


    public void setFeatures(Feature[][] features) {
        this.features = features;
    }

    public String toString() {
        String string = "";
        string += "files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n";

        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) string += features[i][j];
        }
        FileHandler.writeStringToFile("C:\\Users\\gzq\\Desktop\\out.csv", string);
        return string;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public int getFilterNumber() {
        return filterNumber;
    }

    /**
     * 打印bucketsNames
     */
    public void printBucketNames() {
        for (int i = 0; i < bucketNames.length; i++) {
            System.out.print(bucketNames[i] + " " + features[i].length + "||");
        }
        System.out.println();
    }

    public static void setFeatureNames(String[] feature, int labelIndex, int... abandonIndex) {
        featureNames = new String[feature.length - 1 - abandonIndex.length];
        for (int i = 0, j = 0; i < featureNames.length; j++) {
            // label attribute
            if (j == labelIndex) continue;

            // abandon attribute
            boolean isAbandon = false;
            for (int abandon : abandonIndex)
                if (j == abandon) {
                    isAbandon = true;
                    break;
                }
            if (isAbandon) continue;

            //useful feature
            featureNames[i++] = feature[j].replace("\"", "");
        }
    }

    public static String getFeatureNames(Object index) {
        return featureNames[(Integer) index];
    }
}
