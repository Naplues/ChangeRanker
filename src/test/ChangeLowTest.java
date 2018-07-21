package test;

/**
 * ChangeLocator 在low dataset上的性能
 */
public class ChangeLowTest {
    /*
    public static void test(String[] versions) {
        Bucket[] buckets = new Bucket[versions.length];
        for (int i = 0; i < versions.length; i++) {
            Bucket bucket = new Bucket(versions[i], Util.changeLocatorPath);
            buckets[i] = bucket;
        }
        Evaluation.evaLowDataset(buckets, true);
    }*/


    public static void main(String[] args) {
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        //test(versions);
    }
}
