import nju.gzq.selector.Selector;
import test.PidTest;

public class MySelector extends Selector {

    @Override
    public double getValue(Integer[] features) {
        return PidTest.testPid(PidTest.versions, features)[3];
    }

    @Override
    public String getFeatureName(Object valueIndex) {
        switch ((Integer) valueIndex) {
            case 0:
                return "NAF";
            case 1:
                return "RLOCC";
            case 2:
                return "RLOAC";
            case 3:
                return "RLODC";
            case 4:
                return "IADCP";
            case 5:
                return "ITDCR";
            case 6:
                return "RF";
            case 7:
                return "IBF";
            case 8:
                return "CC";
            case 9:
                return "IADCL";
            default:
                return "unknown";
        }
    }
}