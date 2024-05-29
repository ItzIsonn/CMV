package me.itzisonn_.cmv.lang.types;

import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;

public enum Type {
    ANY,
    INT,
    FLOAT,
    BOOL,
    STRING;

    public static Type ofString(String string) {
        if (string == null) throw new RuntimeException(Main.getGlobal().getLineNumber(), "type mustn't be null");

        for (Type type : values()) {
            if (type.name().equals(string.toUpperCase())) return type;
        }

        throw new RuntimeException(Main.getGlobal().getLineNumber(), "unknown type \"" + string + "\"");
    }

    public static Type getType(Object value) {
        if (value instanceof Integer) return INT;
        if (value instanceof Float) return FLOAT;
        if (value instanceof Boolean) return BOOL;
        if (value instanceof String) return STRING;
        return ANY;
    }

    public static boolean isType(Object value, Type type) {
        boolean returnValue = false;

        switch (type) {
            case ANY -> returnValue = true;
            case INT -> {
                if (value instanceof Integer) returnValue = true;
            }
            case FLOAT -> {
                if (value instanceof Float) returnValue = true;
            }
            case BOOL -> {
                if (value instanceof Boolean) returnValue = true;
            }
            case STRING -> {
                if (value instanceof String) returnValue = true;
            }
        }

        return returnValue;
    }

    public static Object parseValue(Object value, Type type) {
        Object returnValue = null;

        switch (type) {
            case ANY -> returnValue = value;
            case INT -> {
                try {
                    returnValue = Integer.parseInt(value.toString());
                }
                catch (NumberFormatException ignore) {
                    throw new RuntimeException(Main.getGlobal().getLineNumber(), "couldn't parse value to type " + type.name());
                }
            }
            case FLOAT -> {
                try {
                    returnValue = Float.parseFloat(value.toString());
                }
                catch (NumberFormatException ignore) {
                    throw new RuntimeException(Main.getGlobal().getLineNumber(), "couldn't parse value to type " + type.name());
                }
            }
            case BOOL -> {
                if (Utils.isBool(value)) returnValue = Boolean.parseBoolean(value.toString());
                else throw new RuntimeException(Main.getGlobal().getLineNumber(), "couldn't parse value to type " + type.name());
            }
            case STRING -> returnValue = value.toString();
        }

        return returnValue;
    }
}