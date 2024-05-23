package me.itzisonn_.cmv.lang.main;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.cmv.Main;
import me.itzisonn_.cmv.Utils;
import me.itzisonn_.cmv.lang.exceptions.RuntimeException;
import me.itzisonn_.cmv.lang.types.VoidType;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Variable {
    private final ArrayList<String> bannedNames = new ArrayList<>(List.of("var", "val", "true", "false", "function", "for", "while"));
    private final String name;
    private Object value;
    private final boolean isConst;

    public Variable(String name, String value, boolean isConst) {
        if (!name.matches("[a-zA-Z_]*"))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can only contain English alphabet's letters and underscores");

        if (bannedNames.contains(name) || Utils.isNumeric(name))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can't be keyword or numeric");

        this.name = name;
        this.value = value;
        this.isConst = isConst;
    }

    public Variable(String name, boolean isConst) {
        if (!name.matches("[a-zA-Z_]*"))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can only contain English alphabet's letters and underscores");

        if (bannedNames.contains(name) || Utils.isNumeric(name))
            throw new RuntimeException(Main.getGlobal().getLineNumber(), "variable's name can't be keyword or numeric");

        this.name = name;
        this.value = new VoidType();
        this.isConst = isConst;
    }

    public void setValue(String value) {
        if (isConst) throw new RuntimeException(Main.getGlobal().getLineNumber(), "can't reassign the value of constant variable");

        this.value = value;
    }
}