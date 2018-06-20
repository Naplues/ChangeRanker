import util.*;

public class Main {


    public static void main(String[] args) {
        // 项目版本
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        // 测试特征
        //Test.testSingleFeature(versions);
        //Test.testGroupFeature(versions);
        // 测试Pi方法
        Test.testPi(versions, Feature.POS, Feature.ISCOMPONENT);
        Test.testPi(versions, Feature.POS, Feature.DISTANCE);
        Test.testPi(versions, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
    }
}
