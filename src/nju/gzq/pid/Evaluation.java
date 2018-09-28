package nju.gzq.pid;

import nju.gzq.base.BaseFeature;
import nju.gzq.base.BaseProject;

import java.util.HashSet;
import java.util.Set;

/**
 * 对结果进行评估
 * recall:召回率
 * MAP: 平均准确度均值
 * MRR: 首位倒数排均值
 */
public class Evaluation {

    /**
     * 召回率
     *
     * @param bucket
     * @param k
     * @return
     */
    public static double recall(Project bucket, int k) {
        double rc = 0;

        BaseFeature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < k && j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    rc++;
                    break;
                }
            }
        }
        rc /= bucket.getRevisionNumber();
        return rc;
    }

    /**
     * MRR
     *
     * @param bucket
     * @return
     */
    public static double MRR(Project bucket) {
        double mrr = 0.0;
        BaseFeature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    mrr += 1.0 / (j + 1);
                    break;
                }
            }
        }
        mrr /= bucket.getRevisionNumber();
        return mrr;
    }

    /**
     * MAP
     *
     * @param bucket
     * @return
     */
    public static double MAP(Project bucket) {
        double map = 0.0;
        BaseFeature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {
            double ap = 0.0;
            int k = 0;
            for (int j = 0; j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    ap += (double) (k + 1) / (j + 1);
                    k++;
                }
            }
            ap /= k;
            map += ap;
        }
        map /= bucket.getRevisionNumber();
        return map;
    }

    public static double[] evaluation(Project[] buckets, boolean details) {

        double r1 = 0.0, r5 = 0.0, r10 = 0.0, map = 0.0, mrr = 0.0;
        for (Project bucket : buckets) {
            r1 += Evaluation.recall(bucket, 1);
            r5 += Evaluation.recall(bucket, 5);
            r10 += Evaluation.recall(bucket, 10);
            mrr += Evaluation.MRR(bucket);
            map += Evaluation.MAP(bucket);
            if (details)
                System.out.println(Evaluation.recall(bucket, 1) + "," + Evaluation.recall(bucket, 5) +
                        "," + Evaluation.recall(bucket, 10) + "," + Evaluation.MRR(bucket) + "," + Evaluation.MAP(bucket) +
                        ", " + bucket.getFilterNumber() + "/" + bucket.getRevisionNumber());
        }

        //输出到控制台
        double[] values = new double[5];
        values[0] = r1 / buckets.length;
        values[1] = r5 / buckets.length;
        values[2] = r10 / buckets.length;
        values[3] = mrr / buckets.length;
        values[4] = map / buckets.length;

        for (int i = 0; i < values.length; i++) {
            System.out.print(values[i] + ",");
        }
        //System.out.println("\n================================================================================");
        System.out.println();
        return values;
    }


    /**
     * 准确率
     *
     * @param bucket
     * @param k
     * @return
     */
    public static double precision(Project bucket, int k) {
        double rc = 0;
        BaseFeature[][] features = bucket.getFeatures();
        for (int i = 0; i < features.length; i++) {

            for (int j = 0; j < k && j < features[i].length; j++) {
                if (features[i][j].isLabel()) {
                    rc++;
                    break;
                }
            }
        }

        rc /= bucket.getFilterNumber();
        return rc;
    }

    /**
     * 评估候选集较少的数据集
     *
     * @param buckets
     */
    public static void evaLowDataset(Project[] buckets, boolean details) {
        double r1 = 0.0, r2 = 0.0, map = 0.0, mrr = 0.0;
        for (Project bucket : buckets) {
            r1 += Evaluation.precision(bucket, 1);
            r2 += Evaluation.precision(bucket, 2);
            mrr += Evaluation.MRR(bucket);
            map += Evaluation.MAP(bucket);
            if (details)
                System.out.println(Evaluation.precision(bucket, 1) + "," + Evaluation.precision(bucket, 2) + "," +
                        Evaluation.MRR(bucket) + "," + Evaluation.MAP(bucket) + ", " + bucket.getFilterNumber());
        }

        //输出到控制台
        System.out.print(r1 / buckets.length + ",");
        System.out.print(r2 / buckets.length + ",");
        System.out.print(mrr / buckets.length + ",");
        System.out.println(map / buckets.length);
    }
}
