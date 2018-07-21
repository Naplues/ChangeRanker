

public class Main {

    public static void main(String[] args) throws Exception {

        // 测试单个特征
        //PidTest.testSingleFeature(PidTest.versions);
        // 测试两个特征
        //PidTest.testGroupFeature(PidTest.versions);
        // 测试Pi方法
        //PidTest.testPid(PidTest.versions, BaseFeature.POS, BaseFeature.ISCOMPONENT);
        //PidTest.testPid(PidTest.versions, BaseFeature.POS, BaseFeature.DISTANCE);
        // 测试Pid方法
        //PidTest.testPid(PidTest.versions, 1, 5, 9);
        // 测试Pid在候选较少的buckets上的结果
        //PidTest.testLowDataSet(PidTest.versions, 10, BaseFeature.POS, BaseFeature.DISTANCE, BaseFeature.ISCOMPONENT);
        //PidTest.testLowDataSet(PidTest.versions, 5, BaseFeature.POS, BaseFeature.DISTANCE, BaseFeature.ISCOMPONENT);
        //测试更多特征组合
        //PidTest.testMoreFeature(versions);

        //测试特征选择器
        testSelector();

    }

    /**
     * 测试特征选择器
     *
     * @throws Exception
     */
    public static void testSelector() throws Exception {
        //选择特征
        int featureNumber = 10;
        int neededFeatureNumber = 10;
        double threshold = 0.0;
        String outputPath = "C:\\Users\\gzq\\Desktop\\test";
        String fileType = "svg";
        int top = 10;
        new MySelector().start(featureNumber, outputPath, fileType, neededFeatureNumber, threshold, false, top, false);
    }
}
