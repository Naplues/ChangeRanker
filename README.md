# PLC: A simple combination based approach to locating crash-inducing changes

**Abstract**. Please Wait.   

**Keywords**: Please Wait.

## PLC
This project implements the PLC approach.

## Data Set:
All changes feature data are collected by [Wu et al.](http://101.96.10.64/sccpu2.cse.ust.hk/castle/materials/ChangeLocator.pdf)  

rootPath (In Util.java): NetBeans Data folder root path. Data set can be download directly in data folder.

## Selector.jar
This is a tool for selecting feature combinations from a feature set. You can use this tool in your research. Fisrtly, import this jar archive into your project. Secondly, create a subclass extending the super class Selector.java and override the method **double getValue(Integer[] features)** and **String getFeatureName(Object valueIndex)**. Then, you can select your only feature combinations. The source code about Selector can also be found in the selector package.

## Contact
email: naplus@smail.nju.edu.cn
