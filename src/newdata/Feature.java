package newdata;

public class Feature {
    public static int FIX = 0;
    public static int HACK = 1;
    public static int SHOULD = 2;
    public static int TODO = 3;
    public static int WORKAROUND = 4;


    private Double fix;
    private Double hack;
    private Double should;
    private Double todo;
    private Double workaround;
    private boolean isInducing;
    private Double temp;

    public Feature(String[] values) {
        Double[] number = new Double[values.length - 1];
        for (int i = 1; i < values.length; i++) {
            number[i - 1] = Double.parseDouble(values[i]);
        }
        this.fix = number[0];
        this.hack = number[1];
        this.should = number[2];
        this.todo = number[3];
        this.workaround = number[4];

        if (values[0].equals("1")) this.isInducing = true;
        else this.isInducing = false;
    }

    public Feature(String[] values, boolean is) {

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
                return fix;
            case 1:
                return hack;
            case 2:
                return should;
            case 3:
                return todo;
            case 4:
                return workaround;
            default:
                return fix;
        }
    }

    public Double getFix() {
        return fix;
    }

    public void setFix(Double fix) {
        this.fix = fix;
    }

    public Double getHack() {
        return hack;
    }

    public void setHack(Double hack) {
        this.hack = hack;
    }

    public Double getShould() {
        return should;
    }

    public void setShould(Double should) {
        this.should = should;
    }


    public Double getTodo() {
        return todo;
    }

    public void setTodo(Double todo) {
        this.todo = todo;
    }

    public Double getWorkaround() {
        return workaround;
    }

    public void setWorkaround(Double workaround) {
        this.workaround = workaround;
    }

    public boolean isInducing() {
        return isInducing;
    }

    public void setInducing(boolean inducing) {
        isInducing = inducing;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
