package me.itzisonn_.cmv;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern SPACES = Pattern.compile("(\"[^\"]*(\"|$))|\\s+");
    
    public static Object parseValue(String value) {
        if (value == null) return null;

        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ignore) {}

        try {
            return Float.parseFloat(value);
        }
        catch (NumberFormatException ignore) {}

        if (value.equals("true")) return true;
        if (value.equals("false")) return false;

        return value;
    }

    public static boolean isBool(String value) {
        return value.equals("true") || value.equals("false");
    }

    public static boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ignore) {}

        try {
            Float.parseFloat(value);
            return true;
        }
        catch (NumberFormatException ignore) {}

        return false;
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException ignore) {}

        return false;
    }

    public static String removeDuplicatedSpaces(String string) {
        return SPACES.matcher(string).replaceAll(mr -> null == mr.group(1) ? " " : mr.group(1));
    }

    public static ArrayList<String> split(String string, char character) {
        ArrayList<String> split = new ArrayList<>();
        string += character;
        StringBuilder current = new StringBuilder();
        boolean isIn = false;

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '"') {
                isIn = !isIn;
            }
            if (string.charAt(i) == character && !isIn) {
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