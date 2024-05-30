package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.types.Type;
import me.itzisonn_.cmv.lang.types.VoidValue;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
public class Variable {
    private final ArrayList<String> bannedNames = new ArrayList<>(List.of("var", "val", "true", "false", "function", "for", "while"));
    @Getter
    protected final String name;
    @Getter
    protected final Type type;
    @Getter
    protected Object value;
    @Getter
    protected final boolean isConst;

    public Variable(String name, Type type, Object value, boolean isConst) {
        if (!name.matches("[a-zA-Z_]+"))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can only contain English alphabet's letters and underscores");

        if (bannedNames.contains(name) || Utils.isInt(name) || Utils.isFloat(name))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can't be keyword or numeric");

        this.name = name;
        this.type = type;
        setValue(value);
        this.isConst = isConst;
    }

    public void setValue(Object value) {
        if (isConst) throw new RuntimeException(Main.getGlobal().getLineNumber(), "can't reassign the value of constant variable");

        if (value instanceof VoidValue || type == Type.ANY || Type.isType(value, type)) {
            this.value = value;
            return;
        }

        throw new RuntimeException(Main.getGlobal().getLineNumber(), "expected " + type + " value but found " + Type.getType(value));
    }
}