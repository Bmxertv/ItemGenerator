package de.bmxertv.itemgenerator.exception;

import org.bukkit.configuration.ConfigurationSection;

public class DeserializeException extends Exception {

    public DeserializeException(String model, ConfigurationSection section) {
        super("Key %s, in %s can't deserialize because the Model is not Complete".formatted(section.getName(), model));
    }
}
