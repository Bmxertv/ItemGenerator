package de.bmxertv.itemgenerator.model;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class GeneratorModel {

    private int id;
    private int x;
    private int y;
    private int z;
    private String world;
    private ItemStack item;
    private int full;
    private int generated;

    public GeneratorModel() {
    }

    public GeneratorModel(int id, Location location, ItemStack item, int full, int generated) {
        this.id = id;
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
        this.world = location.getWorld().getName();
        this.item = item;
        this.full = full;
        this.generated = generated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getFull() {
        return full;
    }

    public void setFull(int full) {
        this.full = full;
    }

    public int getGenerated() {
        return generated;
    }

    public void setGenerated(int generated) {
        this.generated = generated;
    }

    public ConfigurationSection serialize(ConfigurationSection mainSection) {
        ConfigurationSection section = mainSection.createSection(String.valueOf(this.id));
        ConfigurationSection location = section.createSection("location");
        location.set("x", this.x);
        location.set("y", this.y);
        location.set("z", this.z);
        location.set("world", this.world);

        section.set("full", 0);
        section.set("generated", 0);

        return section;
    }

}
