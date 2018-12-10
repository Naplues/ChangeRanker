package main;

import nju.gzq.selector.Selector;
import test.PLCTest;

import java.util.List;

public class MySelector extends Selector {
    @Override
    public double getValue(List<String> trainVersions, Integer[] features) {
        return PLCTest.testPLC(trainVersions, features)[3];
    }
}