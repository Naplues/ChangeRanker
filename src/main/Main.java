package main;

import test.PLCTest;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // 测试PLC方法
        List<String> versions = new ArrayList<>();
        for (int i = 1; i < PLCTest.versions.length; i++) versions.add(PLCTest.versions[i]);
        //PLCTest.trainFeatureCombination(versions, true, 1, 3, 4);

        //测试特征选择器
        PLCTest.testSelector();

        // 测试Pid在候选较少的buckets上的结果
        //测试更多特征组合
        //PLCTest.testMoreFeature();

    }
}
