package newdata;

import util.Bucket;

import java.io.File;

public class Evaluation {

    /**
     * 输出数据集
     *
     * @param bucket
     */
    public static void print(Bucket bucket) {
        Feature[] feature = bucket.getFeatures()[0];  //特征
        for (Feature f : feature)
            System.out.println(f.getFix() + ", " + f.getHack() + ", " + f.getShould() + ",||" + f.getTemp() + ",||" + f.isInducing());
        System.out.println(feature.length);
    }

    /**
     * F1 value
     * F1 = 2 * P * R / (P + R)
     * @param bucket
     * @return
     */
    public static Double F1(Bucket bucket) {
        Double F1value = .0;
        Feature[] feature = bucket.getFeatures()[0];  //特征
        int position = 0;         //计数位置
        int total = 0;            //所有正例数目
        for (int i = 0; i < feature.length; i++) {
            if (feature[i].getTemp() != 0) position++;
            if (feature[i].isInducing()) total++;
        }
        System.out.println(position + ", " + total);

        Double positive = .0;
        for (int i = 0; i < position; i++) if (feature[i].isInducing()) positive++;
        System.out.println(positive);

        Double P = positive / position; // 32/47
        Double R = positive / total;    // 32/195
        F1value = 2 * P * R / (P + R);
        System.out.println("Recall=" + R + " Precision=" + P + " F1=" + F1value);
        return F1value;
    }

    /**
     * 评估
     *
     * @return
     */
    public static Double evaluation(Integer[] features) {

        String path = "C:\\Users\\gzq\\Desktop\\data\\";
        File[] projects = new File(path).listFiles();

        Double value = .0;
        for (File project : projects) {
            Bucket bucket = new Bucket(project.getPath());
            bucket.setFeatures(Ranking.rankByFeature(bucket, features));
            value += Evaluation.F1(bucket);
        }
        value /= projects.length;
        return value;
    }
}
