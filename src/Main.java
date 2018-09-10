import nju.gzq.selector.FileHandle;
import test.PidTest;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        // 测试单个特征
        //PidTest.testSingleFeature(PidTest.versions);
        // 测试两个特征
        //PidTest.testGroupFeature(PidTest.versions);
        // 测试Pid在候选较少的buckets上的结果
        //PidTest.testLowDataSet(PidTest.versions, 10, BaseFeature.POS, BaseFeature.DISTANCE, BaseFeature.ISCOMPONENT);
        //PidTest.testLowDataSet(PidTest.versions, 5, BaseFeature.POS, BaseFeature.DISTANCE, BaseFeature.ISCOMPONENT);
        //测试更多特征组合
        PidTest.testMoreFeature(PidTest.versions);


        // 测试Pid方法
        //PidTest.testPid(PidTest.versions, 4, 8, 9);
        //测试特征选择器
        //testSelector();

        //combine();
    }


    public static void combine() {
        String filePath = "C:\\Users\\gzq\\Desktop\\git\\Pid\\buckets_data\\form1\\6.5";
        File[] files = new File(filePath).listFiles();
        String text = "";
        String head = "";
        for (File file : files) {
            List<String> lines = FileHandle.readFileToLines(file.getPath());
            head = lines.get(0) + "\n";
            for (int i = 1; i < lines.size(); i++) {
                text += lines.get(i) + "\n";
            }
        }
        text = head + text;
        System.out.println(text);
        FileHandle.writeStringToFile("C:\\Users\\gzq\\Desktop\\result1.csv", text);
    }

    /**
     * 测试特征选择器
     *
     * @throws Exception
     */
    public static void testSelector() throws Exception {
        //选择特征
        int featureNumber = 10;
        int neededFeatureNumber = 10;
        double threshold = 0.0;
        String outputPath = "C:\\Users\\gzq\\Desktop\\result";
        String fileType = "svg";
        int top = 10;
        new MySelector().start(featureNumber, outputPath, fileType, neededFeatureNumber, threshold, false, top,false);
    }
}
