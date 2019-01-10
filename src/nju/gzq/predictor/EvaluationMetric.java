package nju.gzq.predictor;

import java.util.List;

public class EvaluationMetric {
    public EvaluationMetric() {
    }

    public static double MRR(List<List<Integer>> ranks) {
        double ans = 0.0D;
        double[] array = new double[ranks.size()];
        for (int i = 0; i < ranks.size(); ++i) {
            List<Integer> rank = (List) ranks.get(i);
            ans += RR(rank);
            array[i] = RR(rank);
        }
        //printArray(array);
        return ans / (double) ranks.size();
    }

    public static double RR(List<Integer> rank) {
        if (rank.size() == 0) {
            return 0.0D;
        } else {
            double ans = 1.0D / (double) (rank.get(0) + 1);
            return ans;
        }
    }


    public static double MAP(List<List<Integer>> ranks) {
        double ans = 0.0D;
        double[] array = new double[ranks.size()];
        for (int i = 0; i < ranks.size(); ++i) {
            ans += AP((List) ranks.get(i));
            array[i] = AP((List) ranks.get(i));
        }
        //printArray(array);
        return ans / (double) ranks.size();
    }

    public static double AP(List<Integer> rank) {
        double tmp = 0.0D;
        if (rank.size() == 0) {
            return tmp;
        } else {
            for (int j = 0; j < rank.size(); ++j) {
                int r = j + 1;
                tmp += (double) r * 1.0D / (double) ((Integer) rank.get(j) + 1);
            }

            return tmp / (double) rank.size();
        }
    }

    public static double[] topN(List<List<Integer>> ranks, int N) {
        int[] array = new int[ranks.size()];
        int[] rank = new int[N];
        double[] results = new double[N];

        int[] N049 = {0, 4, 9};
        for (int n = 0; n < N049.length; n++) {
            for (int i = 0; i < ranks.size(); i++) {
                for (Integer r : ranks.get(i)) {
                    if (r <= N049[n]) {
                        array[i] = 1;
                        break;
                    }
                }
            }
            //printArray(array);
        }

        int tot;
        int tmp;
        for (tot = 0; tot < ranks.size(); ++tot) {
            if ((ranks.get(tot)).size() > 0) {
                tmp = (Integer) ((List) ranks.get(tot)).get(0);
                if (tmp < N) {
                    ++rank[tmp];
                }
            }
        }

        tot = 0;

        for (tmp = 0; tmp < N; ++tmp) {
            results[tmp] = (double) (rank[tmp] + tot) * 1.0D / (double) ranks.size();
            tot += rank[tmp];
        }

        return results;
    }

    public static double[] topNMCR(List<List<Integer>> ranks, int N) {
        double[][] results = new double[ranks.size()][N];

        int j;
        for (int i = 0; i < ranks.size(); ++i) {
            int[] rank = new int[N];

            for (int index = 0; index < ranks.get(i).size() && (Integer) ((List) ranks.get(i)).get(index) < N; ++index) {
                ++rank[(Integer) ((List) ranks.get(i)).get(index)];
            }

            int tot = 0;

            for (j = 0; j < N; ++j) {
                results[i][j] = (double) (rank[j] + tot) * 1.0D / (double) Math.min(j + 1, (ranks.get(i)).size());
                tot += rank[j];
            }
        }

        double[] finalResults = new double[N];

        for (int i = 0; i < N; ++i) {
            double tot = 0.0D;

            for (j = 0; j < ranks.size(); ++j) {
                tot += results[j][i];
            }

            finalResults[i] = tot / (double) ranks.size();
        }

        return finalResults;
    }

    public static void printArray(int[] array) {
        for (int ele : array) System.out.print(ele + ", ");
        System.out.println();
    }

    public static void printArray(double[] array) {
        for (double ele : array) System.out.print(ele + ", ");
        System.out.println();
    }
}
