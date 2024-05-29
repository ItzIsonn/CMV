package me.itzisonn_.cmv;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {
    @Getter
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern SPACES = Pattern.compile("(\"[^\"]*(\"|$))|\\s+");

    public static boolean isBool(Object value) {
        if (value instanceof Boolean) return true;
        return value.equals("true") || value.equals("false");
    }

    public static boolean isNumeric(Object value) {
        try {
            Integer.parseInt(value.toString());
            return true;
        }
        catch (NumberFormatException ignore) {}

        try {
            Float.parseFloat(value.toString());
            return true;
        }
        catch (NumberFormatException ignore) {}

        return false;
    }

    public static boolean isInt(Object value) {
        try {
            Integer.parseInt(value.toString());
            return true;
        }
        catch (NumberFormatException ignore) {
            if (isNumeric(value)) {
                return value.toString().endsWith(".0");
            }

            return false;
        }
    }

    public static Object convertBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            if (Utils.isInt(((BigDecimal) value).longValue())) {
                return ((BigDecimal) value).intValueExact();
            }
            else {
                return ((BigDecimal) value).floatValue();
            }
        }
        return value;
    }

    public static String removeDuplicatedSpaces(String string) {
        return SPACES.matcher(string).replaceAll(mr -> null == mr.group(1) ? " " : mr.group(1));
    }

    public static ArrayList<String> split(String string, char character) {
        ArrayList<String> split = new ArrayList<>();
        string += character;
        StringBuilder current = new StringBuilder();
        boolean isIn = false;
        int bracketIndex = 0;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '"') {
                isIn = !isIn;
            }
            if (string.charAt(i) == '(') {
                bracketIndex++;
            }
            if (string.charAt(i) == ')') {
                bracketIndex--;
            }
            if (string.charAt(i) == character && !isIn && bracketIndex == 0) {
                split.add(current.toString());
                current = new StringBuilder();
            }
            else {
                current.append(string.charAt(i));
            }
        }

        return split;
    }
}