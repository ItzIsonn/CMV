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
        if (Utils.isInt(value)) return INT;
        if (Utils.isNumeric(value)) return FLOAT;
        if (Utils.isBool(value)) return BOOL;
        return STRING;
    }

    public static boolean isType(Object value, Type type) {
        boolean returnValue = false;

        switch (type) {
            case ANY -> returnValue = true;
            case INT -> {
                if (Utils.isInt(value)) returnValue = true;
            }
            case FLOAT -> {
                if (Utils.isNumeric(value)) returnValue = true;
            }
            case BOOL -> {
                if (Utils.isBool(value)) returnValue = true;
            }
            case STRING -> {
                if (value instanceof String) returnValue = true;
            }
        }

        return returnValue;
    }
}