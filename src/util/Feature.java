package util;

public class Feature {
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


    public Double getFiles() {
        return files;
    }

    public Double getFunctions() {
        return functions;
    }

    public Double getLines() {
        return lines;
    }

    public Double getAddLines() {
        return addLines;
    }

    public Double getDeleteLines() {
        return deleteLines;
    }

    public Double getPos() {
        return pos;
    }

    public Double getTime() {
        return time;
    }

    public Double getRf() {
        return rf;
    }

    public Double getIbf() {
        return ibf;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getIsComponent() {
        return isComponent;
    }

    public boolean isInducing() {
        return isInducing;
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
