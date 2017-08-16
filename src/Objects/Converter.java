package Objects;

/**
 * Created by Myles on 7/11/17.
 */
public class Converter {

    private long longNum;
    private double doubleNum;
    private int intNum;
    private float floatNum;
    private char charAtZero;
    private String string;
    private String stringNum;

    public long getLongNum() {
        return longNum;
    }

    public void setLongNum(long longNum) {
        this.longNum = longNum;
    }

    public double getDoubleNum() {
        return doubleNum;
    }

    public void setDoubleNum(double doubleNum) {
        this.doubleNum = doubleNum;
    }

    public int getIntNum() {
        return intNum;
    }

    public void setIntNum(int intNum) {
        this.intNum = intNum;
    }

    public float getFloatNum() {
        return floatNum;
    }

    public void setFloatNum(float floatNum) {
        this.floatNum = floatNum;
    }

    public char getCharAtZero() {
        return charAtZero;
    }

    public void setCharAtZero(char charAtZero) {
        this.charAtZero = charAtZero;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getStringNum() {
        return stringNum;
    }

    public void setStringNum(String stringNum) {
        this.stringNum = stringNum;
    }

    public void dataConverter(Object obj)
    {
        if(obj instanceof Number)
        {
            longNum = ((Number) obj).longValue();
            doubleNum = ((Number) obj).doubleValue();
            if(((Number) obj).longValue() < Integer.MAX_VALUE) {
                intNum = ((Number) obj).intValue();
            }
            else {
                intNum = -1;
            }
            floatNum = ((Number) obj).floatValue();
        }
        else if(obj instanceof String)
        {
            if(((String) obj).length() > 0) {
                charAtZero = ((String) obj).charAt(0);
            }
            string = (String)obj;
        }
    }

    public Object returnObject(Class type)
    {
        switch (type.getName())
        {
            case "java.lang.String":
                return string;
            case "java.lang.Character":
                return charAtZero;
            case "java.lang.Integer":
                return intNum;
            case "java.lang.Long":
                return longNum;
            case "java.lang.Double":
                return doubleNum;
            case "java.lang.Float":
                return floatNum;
            case "char":
                return charAtZero;
            case "int":
                return intNum;
            case "long":
                return longNum;
            case "double":
                return doubleNum;
            case "float":
                return floatNum;

        }
        return 0;
    }

    public static void main(String[] args) {
        Converter converter = new Converter();
        converter.dataConverter(492839483874584L);
        //System.out.println(converter.getIntNum());
    }
}
