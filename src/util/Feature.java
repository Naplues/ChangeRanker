package util;

/**
 * 一个change的各特征值对象
 */
public class Feature {

    public static int FILES = 0;
    public static int FUNCTIONS = 1;
    public static int LINES = 2;
    public static int ADDLINES = 3;
    public static int DELETELINES = 4;
    public static int POS = 5;
    public static int TIME = 6;
    public static int RF = 7;
    public static int IBF = 8;
    public static int DISTANCE = 9;
    public static int ISCOMPONENT = 10;


    private Double files;
    private Double functions;
    private Double lines;
    private Double addLines;
    private Double deleteLines;
    private Double pos;
    private Double time;
    private Double rf;
    private Double ibf;
    private Double distance;
    private Double isComponent;
    private boolean isInducing;

    public Feature(String[] values) {

        Double[] number = new Double[values.length - 1];
        for (int i = 1; i < number.length; i++) {
            number[i] = Double.parseDouble(values[i]);
        }

        this.files = number[1];
        this.functions = number[2];
        this.lines = number[3];
        this.addLines = number[4];
        this.deleteLines = number[5];
        this.pos = number[6];
        this.time = number[7];
        this.rf = number[8];
        this.ibf = number[9];
        this.isComponent = number[10];
        this.distance = number[11];
        if (values[values.length - 1].equals("true")) this.isInducing = true;
        else this.isInducing = false;
    }

    public Feature(String[] values, boolean isChangeLocator){
        if(!isChangeLocator) return;
        this.files = 0.0;
        this.functions = 0.0;
        this.lines = 0.0;
        this.addLines = .0;
        this.deleteLines = .0;
        this.pos = .0;
        this.time = .0;
        this.rf = .0;
        this.ibf = .0;
        this.isComponent = .0;
        this.distance = .0;
        if (values[values.length - 1].equals("true")) this.isInducing = true;
        else this.isInducing = false;
    }
    public boolean isInducing() {
        return isInducing;
    }


    /**
     * 根据名称获取值
     *
     * @param valueName
     * @return
     */
    public Double getValue(int valueName) {
        switch (valueName) {
            case 0:
                return files;
            case 1:
                return functions;
            case 2:
                return lines;
            case 3:
                return addLines;
            case 4:
                return deleteLines;
            case 5:
                return pos;
            case 6:
                return time;
            case 7:
                return rf;
            case 8:
                return ibf;
            case 9:
                return distance;
            case 10:
                return isComponent;
            default:
                return pos;
        }
    }


    public String toString() {
        String string = "";
        string += files + ",";
        string += functions + ",";
        string += lines + ",";
        string += addLines + ",";
        string += deleteLines + ",";
        string += pos + ",";
        string += time + ",";
        string += rf + ",";
        string += ibf + ",";
        string += isComponent + ",";
        string += distance + ",";
        string += isInducing + "\n";
        return string;
    }
}
