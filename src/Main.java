import selector.Selector;
import test.PidTest;

public class Main {


    public static void main(String[] args) throws Exception {

        // 测试单个特征
        //PidTest.testSingleFeature(versions);
        // 测试两个特征
        //PidTest.testGroupFeature(versions);
        // 测试Pi方法
        //PidTest.testPid(versions, Feature.POS, Feature.ISCOMPONENT);
        //PidTest.testPid(versions, Feature.POS, Feature.DISTANCE);
        // 测试Pid方法
        //PidTest.testPid(PidTest.versions, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
        //PidTest.testLowDataSet(versions, 10, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
        //PidTest.testLowDataSet(versions, 5, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);

        //PidTest.testMoreFeature(versions);

        //选择特征
        int featureNumber = 10;
        int neededFeatureNumber = 10;
        double threshold = 0.7382;
        String outputPath = "C:\\Users\\gzq\\Desktop\\test";

        new MySelector().start(featureNumber, outputPath, neededFeatureNumber, threshold);
    }
}

class MySelector extends Selector{
    @Override
    public double getValue(Integer[] features) {
        return PidTest.testPid(PidTest.versions, features)[3];
    }
}