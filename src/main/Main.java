package main;

import test.PLCTest;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        // 测试PLC方法
        List<String> versions = new ArrayList<>();
        for (int i = 1; i < PLCTest.versions.length; i++) versions.add(PLCTest.versions[i]);
        // PLCTest.testPLC(versions, 1, 3, 4);

        // 测试单个特征
        //PLCTest.testSingleFeature(PLCTest.versions);
        // 测试Pid在候选较少的buckets上的结果
        //PLCTest.testLowDataSet(10, 4,8,9);
        //PLCTest.testLowDataSet(5, 4, 8, 9);
        //测试更多特征组合
        //PLCTest.testMoreFeature();

        //测试特征选择器
        PLCTest.testSelector();
    }

}
