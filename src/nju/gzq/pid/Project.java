package nju.gzq.pid;

import nju.gzq.base.BaseFeature;
import nju.gzq.selector.FileHandle;

import java.io.File;
import java.util.List;

/**
 * feature[][]: 每一行代表一个bucket, 每一个元素代表一个bucket中的一个revision的特征记录
 */
public class Project {
    private BaseFeature[][] features;

    private String versionName;
    private String[] bucketNames;
    private int revisionNumber; //原来版本数目
    private int filterNumber;   //过滤后的版本数目

    public BaseFeature[][] getFeatures() {
        return features;
    }

    /**
     * Pid: 保存一个项目的所有bucket信息
     *
     * @param path         项目版本号
     * @param isLowDataSet 是否测试少量候选集
     * @param threshold    少量候选集阈值
     */
    public Project(String path, boolean isLowDataSet, int... threshold) {
        File[] revisions = new File(path).listFiles();
        versionName = new File(path).getName();
        revisionNumber = revisions.length;
        bucketNames = new String[revisions.length];
        features = new BaseFeature[revisions.length][];
        for (int i = 0; i < features.length; i++) {
            List<String> lines = FileHandle.readFileToLines(revisions[i].getPath());
            bucketNames[i] = revisions[i].getName();
            features[i] = new BaseFeature[lines.size() - 1];
            for (int j = 1; j < lines.size(); j++) {
                features[i][j - 1] = new BaseFeature(lines.get(j).split(","), 12, 0, 2);
            }
        }
/*
        for (int i = 0; i < features.length; i++) {
            System.out.print(revisions[i].getName() + ", ");
            int c = 0, t = 0;
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel()) c++;
                t++;
            }
            System.out.println(t + ", " + c);
        }

*/

        selectOracleFeature();  //选择有oracle的bucket
        if (isLowDataSet) selectLowFeature(threshold[0]);

        //output();
        //output(10);
    }

    /**
     * ChangeLocator: 保存一个项目的所有bucket信息
     *
     * @param version 项目版本号
     * @param path    changeLocator数据路径
     */

    public Project(String version, String path, int filter) {
        File[] tempRevisions = new File(path).listFiles(); //对不同的情况需要改变 path + version
        File[] revisions = new File[tempRevisions.length - 1]; //去除Logistic文件
        for (int i = 0; i < revisions.length; i++)
            revisions[i] = tempRevisions[i];

        versionName = version;
        revisionNumber = filter;
        bucketNames = new String[revisions.length];
        features = new BaseFeature[revisions.length][];
        for (int i = 0; i < features.length; i++) {
            List<String> lines = FileHandle.readFileToLines(revisions[i].getPath());
            bucketNames[i] = revisions[i].getName();
            features[i] = new BaseFeature[lines.size()];

            for (int j = 0; j < lines.size(); j++) {
                features[i][j] = new BaseFeature(lines.get(j).split("\t"), 2, 0);
            }
        }
        this.filterNumber = revisions.length;
    }


    /**
     * 选择有oracle的bucket
     */
    public void selectOracleFeature() {
        int[] pass = new int[features.length];
        int count = 0;
        for (int i = 0, j; i < features.length; i++) {
            if (features[i].length == 1) continue;
            for (j = 0; j < features[i].length; j++) if (features[i][j].isLabel()) break;
            if (j == features[i].length) pass[i] = 1;
        }
        // 统计保留多少bucket
        int remain = 0;
        for (int i = 0; i < pass.length; i++) if (pass[i] == remain) count++;

        BaseFeature[][] filterFeature = new BaseFeature[count][];
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
     * 选择候选个数在指定阈值以内的bucket
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

        BaseFeature[][] filterFeature = new BaseFeature[count][];
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


    public void setFeatures(BaseFeature[][] features) {
        this.features = features;
    }

    public String toString() {
        String string = "";
        string += "files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n";

        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) string += features[i][j];
        }
        FileHandle.writeStringToFile("C:\\Users\\naplues\\Desktop\\out.csv", string);
        return string;
    }

    public int getRevisionNumber() {
        return revisionNumber;
    }

    public int getFilterNumber() {
        return filterNumber;
    }

    public String[] getBucketNames() {
        return bucketNames;
    }

    public String getVersionName() {
        return versionName;
    }

    public void output(int threshold) {
        int count = 0, i = 0;
        for (BaseFeature[] bucket : features) {
            if (bucket.length <= threshold && bucket.length > 5) {
                count++;
                System.out.println(bucketNames[i]);
            }
            i++;
        }
        System.out.println(count);
    }

    public void output() {
        int count = 130;
        for (BaseFeature[] bucket : features) {
            count = Math.min(bucket.length, count);
        }
        System.out.println(count);
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
}
