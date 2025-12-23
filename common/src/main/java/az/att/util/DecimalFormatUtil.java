package az.att.util;

import java.text.DecimalFormat;

public class DecimalFormatUtil {

    private static final DecimalFormat decfor = new DecimalFormat("0.00");

    public static Double roundDoubleValue(Double value) {
        return value!=null ? Double.valueOf(decfor.format(value)): null;
    }
}
