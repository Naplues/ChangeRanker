import util.*;
import test.PidTest;

public class Main {


    public static void main(String[] args) {
        // 项目版本
        String[] versions = {"6.7", "6.8", "6.9", "7.0", "7.1", "7.2"};
        // 测试单个特征
        //PidTest.testSingleFeature(versions);
        // 测试两个特征
        PidTest.testGroupFeature(versions);
        // 测试Pi方法
        //PidTest.testPi(versions, Feature.POS, Feature.ISCOMPONENT);
        //PidTest.testPi(versions, Feature.POS, Feature.DISTANCE);
        // 测试Pid方法
        //PidTest.testPi(versions, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
        //PidTest.testLowDataSet(versions, 10, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
        //PidTest.testLowDataSet(versions, 5, Feature.POS, Feature.DISTANCE, Feature.ISCOMPONENT);
    }
}
