package de.bmxertv.itemgenerator.util;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.model.GeneratorModel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class GeneratorConfig {

    private final ItemGenerator itemGenerator = ItemGenerator.getPlugin(ItemGenerator.class);
    private final File file = Paths.get(itemGenerator.getDataFolder().getAbsolutePath(), "generators.yml").toFile();

    private ConfigurationSection getGeneratorsSection(FileConfiguration configuration) {
        String sectionName = "Generators";
        return configuration.getConfigurationSection(sectionName) == null ? configuration.createSection(sectionName) : configuration.getConfigurationSection(sectionName);
    }

    public void save(Block block) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                ConsoleUtil.error("File generators.yml can't create");
            }
        }

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection generatorsSection = getGeneratorsSection(configuration);

        int id = generatorsSection.getKeys(false).size() + 1;
        GeneratorModel model = new GeneratorModel(id, block.getLocation(), null, 0, 0);

        model.serialize(generatorsSection);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(Block block) {
        if (!file.exists()) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection generatorsSection = getGeneratorsSection(configuration);

        Optional<String> generator = getGeneratorId(block);
        generator.ifPresent(id -> {
            ConfigurationSection section = generatorsSection.getConfigurationSection(id);
            for (String key : section.getKeys(true)) {
                section.set(key, null);
            }
            generatorsSection.set(id, null);
        });

        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setItem(int id, ItemStack item) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection itemSection = getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id)).createSection("item");
        itemSection.set("material", item.getType().getKey().getKey());
        itemSection.set("displayName", item.getItemMeta().getDisplayName());
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFull(int id, int full) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id));
        setFull(id, section.getInt("full") + full);
    }

    public void removeFull(int id, int full) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id));
        int temp = section.getInt("full") - full;
        setFull(id, temp < 0 ? 0 : temp);
    }

    public void setFull(int id, int full) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id)).set("full", full);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGenerated(int id, int generated) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id));
        setGenerated(id, section.getInt("generated") + generated);
    }

    public void setGenerated(int id, int generated) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        getGeneratorsSection(configuration).getConfigurationSection(String.valueOf(id)).set("generated", generated);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGenerator(Block block) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return getGeneratorId(block).isPresent();
    }

    public Optional<String> getGeneratorId(Block block) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection generatorsSection = getGeneratorsSection(configuration);
        return generatorsSection.getKeys(false).stream().filter(id -> {
            ConfigurationSection section = generatorsSection.getConfigurationSection(id + ".location");
            Location location = block.getLocation();
            return section.getInt("x") == location.getBlockX() &&
                    section.getInt("y") == location.getBlockY() &&
                    section.getInt("z") == location.getBlockZ() &&
                    section.getString("world").equals(location.getWorld().getName());
        }).findFirst();
    }

    public GeneratorModel getGeneratorModel(Block block) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection generatorsSection = getGeneratorsSection(configuration);
        Optional<String> generatorId = getGeneratorId(block);
        ConfigurationSection section = generatorsSection.getConfigurationSection(generatorId.get());

        ConfigurationSection locationSection = section.getConfigurationSection("location");
        World world = Bukkit.getWorld(locationSection.getString("world"));
        int x = locationSection.getInt("x");
        int y = locationSection.getInt("y");
        int z = locationSection.getInt("z");
        Location location = new Location(world, x, y, z);

        Material material = Material.RED_STAINED_GLASS_PANE;
        String displayName = "&cSelect Item";
        String[] lore = {
                "&7&m--------",
                "",
                "&7Selects the item you",
                "&7currently have in your hand",
                ""
        };

        if (section.getConfigurationSection("item") != null) {
            ConfigurationSection itemSection = section.getConfigurationSection("item");
            material = Material.getMaterial(itemSection.getString("material").toUpperCase());
            displayName = itemSection.getString("displayName");
            lore = new String[0];
        }

        ItemStack itemStack = new ItemBuilder(material).setName(displayName).setLore(lore).create();

        int full = section.getInt("full");
        int generated = section.getInt("generated");

        return new GeneratorModel(Integer.parseInt(generatorId.get()), location, itemStack, full, generated);
    }

    public GeneratorModel getGeneratorModel(int id) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection generatorsSection = getGeneratorsSection(configuration);
        ConfigurationSection section = generatorsSection.getConfigurationSection(String.valueOf(id));

        ConfigurationSection locationSection = section.getConfigurationSection("location");
        World world = Bukkit.getWorld(locationSection.getString("world"));
        int x = locationSection.getInt("x");
        int y = locationSection.getInt("y");
        int z = locationSection.getInt("z");
        Location location = new Location(world, x, y, z);

        Material material = Material.RED_STAINED_GLASS_PANE;
        String displayName = "&cSelect Item";
        String[] lore = {
                "&7&m--------",
                "",
                "&7Selects the item you",
                "&7currently have in your hand",
                ""
        };

        if (section.getConfigurationSection("item") != null) {
            ConfigurationSection itemSection = section.getConfigurationSection("item");
            material = Material.getMaterial(itemSection.getString("material").toUpperCase());
            displayName = itemSection.getString("displayName");
            lore = new String[0];
        }

        ItemStack itemStack = new ItemBuilder(material).setName(displayName).setLore(lore).create();

        int full = section.getInt("full");
        int generated = section.getInt("generated");

        return new GeneratorModel(id, location, itemStack, full, generated);
    }

    public List<GeneratorModel> getGenerators() {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return getGeneratorsSection(configuration)
                .getKeys(false)
                .stream()
                .map(s -> getGeneratorModel(Integer.parseInt(s)))
                .toList();
    }

}
