package az.att.util;

public class DataMaskUtil {

    private static final String CARD_MASK = "#### xxxx xxxx ####";
    private static final String ACCOUNT_MASK = "########xxxxxxxxxxxxxxxx####";

    public static String maskData(String data, String mask) {
        // format the number
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);

            if (c == '#' && data.length() > i) {
                maskedNumber.append(data.charAt(i));
            } else if (data.length() < i) {
                return maskedNumber.toString();
            } else {
                maskedNumber.append('x');
            }
        }

        if (data.length() > mask.length()) {
            maskedNumber.append("x".repeat(data.length() - mask.length()));
        }
        // return the masked number
        return maskedNumber.toString();
    }

    public static String maskAccount(String iban) {
        return iban;//maskData(iban, ACCOUNT_MASK);
    }

    public static String maskCard(String card) {
        return maskData(card, CARD_MASK);
    }

}

