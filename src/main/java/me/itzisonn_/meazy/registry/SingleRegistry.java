package me.itzisonn_.meazy.registry;

import lombok.Getter;

/**
 * Single-entry registry
 * @see RegistryEntry
 * @see RegistryIdentifier
 *
 * @param <T> Entry's value type
 */
@Getter
public class SingleRegistry<T> {
    /**
     * Registered entry
     */
    private RegistryEntry<T> entry;

    /**
     * Registers new entry
     *
     * @param id Identifier to register
     * @param value Value to register
     * @return Given value
     *
     * @throws IllegalArgumentException When not overridable entry is already registered
     */
    public T register(RegistryIdentifier id, T value) {
        return register(id, value, true);
    }

    /**
     * Registers new entry
     *
     * @param id Identifier to register
     * @param value Value to register
     * @param overridable Is this entry overridable
     * @return Given value
     *
     * @throws IllegalArgumentException When not overridable entry is already registered
     */
    public T register(RegistryIdentifier id, T value, boolean overridable) {
        if (hasEntry() && !entry.isOverrideable()) throw new IllegalArgumentException("Registry already has a value!");
        entry = new RegistryEntry<>(id, value, overridable);
        return value;
    }

    /**
     * @return Has registered entry
     */
    public boolean hasEntry() {
        return entry != null;
    }
}
