package main;

import test.PLCTest;

public class Main {

    public static void main(String[] args) {
        // 测试PLC方法
        PLCTest.testFeatureCombination(0, 1, 3, 4);
        //测试特征选择器
        //PLCTest.testSelector();

        // 测试Pid在候选较少的buckets上的结果
        //测试更多特征组合
        //PLCTest.testMoreFeature();

    }
}
