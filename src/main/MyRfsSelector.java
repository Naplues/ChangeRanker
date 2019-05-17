package main;

import nju.gzq.selector.RfsSelector;
import test.PLCTest;

import java.util.List;

public class MyRfsSelector extends RfsSelector {
    public static String form = "Form3";

    @Override
    public double getValue(List<String> trainVersions, Integer[] features) {
        return PLCTest.trainFeatureCombination(form, trainVersions, false, features)[3];
    }
}