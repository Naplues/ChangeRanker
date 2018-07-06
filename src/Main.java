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
        double threshold = 0.0;
        String outputPath = "C:\\Users\\gzq\\Desktop\\test";
        int top = 10;

        new MySelector().start(featureNumber, outputPath, neededFeatureNumber, threshold, false, top);
    }
}

class MySelector extends Selector {

    @Override
    public double getValue(Integer[] features) {
        return PidTest.testPid(PidTest.versions, features)[3];
    }

    @Override
    public String getFeatureName(Object valueIndex) {
        switch ((Integer) valueIndex) {
            case 0:
                return "NAF";
            case 1:
                return "CC";
            case 2:
                return "RLOCC";
            case 3:
                return "RLOAC";
            case 4:
                return "RLODC";
            case 5:
                return "IADCP";
            case 6:
                return "ITDCR";
            case 7:
                return "RF";
            case 8:
                return "IBF";
            case 9:
                return "IADCL";
            default:
                return "IADCP";
        }
    }
}