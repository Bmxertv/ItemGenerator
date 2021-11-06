package de.bmxertv.itemgenerator;

import de.bmxertv.itemgenerator.command.RecipeCommand;
import de.bmxertv.itemgenerator.recipe.GeneratorRecipe;
import de.bmxertv.itemgenerator.util.ConsoleUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemGenerator extends JavaPlugin {

    public final String PREFIX = "&7[&eItemGenerator&7]";
    public final String CONSOLE_PREFIX = "[ItemGenerator]";
    private GeneratorRecipe generatorRecipe;

    @Override
    public void onEnable() {
        ConsoleUtil.info("------%s------".formatted(CONSOLE_PREFIX));
        ConsoleUtil.info("Plugin has started");
        ConsoleUtil.info("Plugin by: %s".formatted(getDescription().getAuthors()));
        ConsoleUtil.info("Plugin Version: %s".formatted(getDescription().getVersion()));
        ConsoleUtil.info("------%s------".formatted(CONSOLE_PREFIX.replaceAll(".", "-")));

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.generatorRecipe = new GeneratorRecipe(this);
        this.generatorRecipe.createRecipe();

        getCommand("generatorRecipe").setExecutor(new RecipeCommand(this));
    }

    @Override
    public void onDisable() {
        ConsoleUtil.info("------%s------".formatted(CONSOLE_PREFIX));
        ConsoleUtil.info("Plugin has stopped");
        ConsoleUtil.info("Plugin by: %s".formatted(getDescription().getAuthors()));
        ConsoleUtil.info("Plugin Version: %s".formatted(getDescription().getVersion()));
        ConsoleUtil.info("------%s------".formatted(CONSOLE_PREFIX.replaceAll(".", "-")));
    }

    public GeneratorRecipe getGeneratorRecipe() {
        return generatorRecipe;
    }
}
