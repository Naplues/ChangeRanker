package main;

import nju.gzq.selector.RfsSelector;
import test.PLCTest;

import java.util.List;

public class MyRfsSelector extends RfsSelector {
    @Override
    public double getValue(List<String> trainVersions, Integer[] features) {
        return PLCTest.trainFeatureCombination("Form3", trainVersions, false, features)[3];
    }
}