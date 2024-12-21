package me.itzisonn_.meazy.runtime.values.statement_info;

import me.itzisonn_.meazy.runtime.values.RuntimeValue;

public class ReturnInfoValue extends StatementInfoValue<RuntimeValue<?>> {
    public ReturnInfoValue(RuntimeValue<?> value) {
        super(value);
    }
}