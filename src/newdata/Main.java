package newdata;

import nju.gzq.selector.Selector;
import util.Bucket;

public class Main {
    public static String name = "average";

    /**
     * 测试特征选择器
     *
     * @throws Exception
     */
    public static void testSelector() {
        //选择特征
        int featureNumber = 5;
        int neededFeatureNumber = 5;
        double threshold = 0.0;
        String outputPath = "C:\\Users\\gzq\\Desktop\\" + name;
        String fileType = "svg";
        int top = 10;
        new MySelector().start(featureNumber, outputPath, fileType, neededFeatureNumber, threshold, false, top, false);
    }

    public static void main(String[] args) {

        String[] projects = {"argouml", "columba-1.4-src", "hibernate-distribution-3.3.2.GA", "jEdit-4.2", "jfreechart-1.0.19", "jmeter-2.10", "jruby-1.4.0", "sql12"};

        for (String project : projects) {
            Bucket bucket = new Bucket("C:\\Users\\gzq\\Desktop\\data\\" + project + ".arff.csv");
            bucket.setFeatures(Ranking.rankByFeature(bucket, 1, 3, 4));
            Evaluation.F1(bucket);
        }

        //testSelector();

    }
}

class MySelector extends Selector {

    @Override
    public double getValue(Integer[] features) {
        return Evaluation.evaluation(features);
    }

    @Override
    public String getFeatureName(Object valueIndex) {
        switch ((Integer) valueIndex) {
            case 0:
                return "fix";
            case 1:
                return "hack";
            case 2:
                return "should";
            case 3:
                return "todo";
            case 4:
                return "workaround";
            default:
                return "unknown";
        }
    }
}