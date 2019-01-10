package nju.gzq.selector.fc;

/**
 * Feature: Built-in Feature class which defines the structure of the feature read from external file (e.g., .csv)
 * An instance denotes the data of a line in .csv file
 */
public class Feature {
    //////////////////////////////////////// Field Definition ////////////////////////////////////////////
    // temporary value of combinatorial features, which will be assigned during feature selection.
    private Double temp;

    private String[] featureName;
    private Double[] featureValue;
    private boolean label;

    //////////////////////////////////////// Method Definition ////////////////////////////////////////////

    /**
     * Default constructor
     */
    public Feature() {

    }

    /**
     * Create a feature instance
     *
     * @param valueString Numeric value of string form
     * @param labelIndex  label index in valueString in array
     */
    public Feature(String[] valueString, int labelIndex, int... abandonIndex) {
        // assign feature value
        featureValue = new Double[valueString.length - 1 - abandonIndex.length];
        for (int i = 0, j = 0; i < featureValue.length; j++) {
            // label attribute
            if (j == labelIndex)
                continue;

            // abandon attribute
            boolean isAbandon = false;
            for (int abandon : abandonIndex)
                if (j == abandon) {
                    isAbandon = true;
                    break;
                }
            if (isAbandon) continue;

            //useful feature
            featureValue[i++] = Double.parseDouble(valueString[j]);
        }

        // assign label value
        this.label = valueString[labelIndex].equals("true");
    }

    /**
     * get value of specified feature index
     *
     * @param valueIndex value index
     * @return
     */
    public Double getValueFromIndex(int valueIndex) {
        return featureValue[valueIndex];
    }


    /////////////////////////////////////// getter and setter /////////////////////////////////////////////
    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String[] getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String[] featureName) {
        this.featureName = featureName;
    }

    public Double[] getFeatureValue() {
        return featureValue;
    }

    public void setFeatureValue(Double[] featureValue) {
        this.featureValue = featureValue;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }
}
