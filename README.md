# ChangeRanker: Boosting crash-inducing change localization with rank-performance-based feature subset

**Abstract**. Please Wait.   

**Keywords**: Please Wait.

## ChangeRanker
This project implements the ChangeRanker.

## Parameters Details
The detailed parameters setting can be fond in the folder [parameter](https://github.com/Naplues/ChangeRanker/blob/master/parameter/)

## Data Set:
All changes feature data are collected by [Wu et al.](http://101.96.10.64/sccpu2.cse.ust.hk/castle/materials/ChangeLocator.pdf)  

rootPath (In Util.java): NetBeans Data folder root path. Data set can be download directly in data folder.

## Selector.jar
This is a tool for selecting feature combinations from a feature set. You can use this tool in your research. Fisrtly, import this jar archive into your project. Secondly, create a subclass extending the super class Selector.java and override the method **double getValue(Integer[] features)** and **String getFeatureName(Object valueIndex)**. Then, you can select your only feature combinations. The source code about Selector can also be found in [here](https://github.com/Naplues/Selector).

## Contact
email: naplus@smail.nju.edu.cn
