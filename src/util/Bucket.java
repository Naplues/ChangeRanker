package util;

import java.io.File;
import java.util.List;

/**
 * feature[][]: 每一行代表一个bucket, 每一个元素代表一个bucket中的一个revision的特征
 */
public class Bucket {
    private String[] bucketNames;

    private Feature[][] features;
    private int revisionNumber; //原来版本数
    private int filterNumber;   //过滤后的版本数

    public Feature[][] getFeatures() {
        return features;
    }

    public Bucket(String bucketDir) {
        File[] revisions = new File(bucketDir).listFiles();
        revisionNumber = revisions.length;
        bucketNames = new String[revisions.length];
        features = new Feature[revisions.length][];
        for (int i = 0; i < features.length; i++) {
            List<String> lines = FileHandle.readFileToLines(revisions[i].getPath());
            bucketNames[i] = revisions[i].getName();
            features[i] = new Feature[lines.size() - 1];
            for (int j = 1; j < lines.size(); j++) {
                features[i][j - 1] = new Feature(lines.get(j).split(","));
            }
        }
        filter();
    }

    // 过滤掉无效的bucket
    public void filter() {
        int[] pass = new int[features.length];
        int count = 0;
        for (int i = 0, j; i < features.length; i++) {
            if (features[i].length == 1) continue;
            for (j = 0; j < features[i].length; j++) if (features[i][j].isInducing()) break;
            if (j == features[i].length) pass[i] = 1;
        }

        for (int i = 0; i < pass.length; i++) if (pass[i] == 0) count++;
        Feature[][] filterFeature = new Feature[count][];
        String[] filterBucketNames = new String[count];
        for (int i = 0, j = 0; i < features.length; i++) {
            if (pass[i] == 0) {
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
        String string = "Bucket: " + bucketNames + "\n";
        string += "files,functions,lines,addLines,deleteLines,pos,time,rf,ibf,isComponent,distance,isInducing\n";

        for (int i = 0; i < features.length; i++) {
            string += bucketNames[i] + features[i].length + "\n";
            for (int j = 0; j < features[i].length; j++) {
                string += features[i][j];
            }
        }
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
}
