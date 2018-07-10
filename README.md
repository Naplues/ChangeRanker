# Pid: A simple unsupervised approach to locating crash-inducing changes

**Abstract**. ~~Many changes would be applied into the code repository because of the business requirements and logic improvements, nevertheless, unexpected crash would occur due to some buggy changes, called crash-inducing changes, which would result in undesired consequence.  To fix the crash induced by these changes, developers usually need to review a great number of codes to locate the position the bug obscured which would consume lots of time and energy. ChangeLocator is the state of the art technology using supervised approach to assist above problem. However, some shortcomings exist in ChangeLocator. In this paper, thus, we propose Pid, a simple unsupervised feature combination approach to locating crash-inducing changes. Our method only applies three main features from ChangeLocator and then give a descending order of all changes according to their suspicious scores computed by the features. We design an experiment on six release versions of NetBeans project to evaluate our approach. The experimental result of Recall@K of Pid can reach 0.514, 0.710 and 0.726 when k is set to 1, 5 and 10 respectively which is better than the recall rate of ChangeLocator. In addition, the value of MAP and MRR is 0.746 and 0.777 is much higher than the former. The result of our experiment shows that the simple unsupervised approach instead of machine learning methods can get an excellent performance in locating crash inducing changes.~~  

**Keywords**: Crash-inducing change, Bug Localization, Unsupervised approach, Crash stack

## Pid
This project implements the Pid approach.

## Data Set:
All changes feature data are collected by [Wu et al.](http://101.96.10.64/sccpu2.cse.ust.hk/castle/materials/ChangeLocator.pdf)  

rootPath (In Util.java): NetBeans Data folder root path. Data set can be download directly in data folder.

## Selector.jar
This is a tool for selecting feature combinations from a feature set. You can use this tool in your research. Fisrtly, import this jar archive into your project. Secondly, create a subclass extending the super class Selector.java and override the method **double getValue(Integer[] features)** and **String getFeatureName(Object valueIndex)**. Then, you can select your only feature combinations. The source code about Selector can also be found in the selector package.

## Contact
email: naplus@smail.nju.edu.cn
