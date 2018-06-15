import util.Bucket;
import util.Evaluation;
import util.Ranking;

public class Main {

    public static void main(String[] args) {
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        Bucket[] buckets = new Bucket[versions.length];

        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket("C:\\Users\\naplues\\Desktop\\ChangeLocator\\pi\\" + versions[i] + "\\");
            bucket.setFeatures(Ranking.rankByTime(bucket));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets);
    }
}
