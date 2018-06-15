import util.Bucket;
import util.Evaluation;
import util.Feature;
import util.Ranking;

public class Main {

    public static void main(String[] args) {
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        Bucket[] buckets = new Bucket[versions.length];

        // for (int n = 0; n < 11; n++) {
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket("C:\\Users\\gzq\\Desktop\\ChangeLocator\\pi\\" + versions[i] + "\\features\\");
            bucket.setFeatures(Ranking.rankByPi(bucket, Feature.FUNCTIONS, Feature.ISCOMPONENT, 0));
            buckets[i] = bucket;
        }
        Evaluation.evaluation(buckets);
        System.out.println();
        //  }
    }
}
