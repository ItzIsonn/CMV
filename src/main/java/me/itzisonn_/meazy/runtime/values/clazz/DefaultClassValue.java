package me.itzisonn_.meazy.runtime.values.clazz;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.itzisonn_.meazy.runtime.environment.interfaces.ClassEnvironment;
import me.itzisonn_.meazy.runtime.values.RuntimeValue;

@Getter
@EqualsAndHashCode(callSuper = false)
public class DefaultClassValue extends RuntimeValue<Object> {
    private final ClassEnvironment classEnvironment;
    private final String id;

    public DefaultClassValue(ClassEnvironment classEnvironment) {
        super(null);
        this.classEnvironment = classEnvironment;
        this.id = classEnvironment.getId();
    }
}
