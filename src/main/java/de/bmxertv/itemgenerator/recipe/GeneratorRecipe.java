package de.bmxertv.itemgenerator.recipe;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.util.ColorUtils;
import de.bmxertv.itemgenerator.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public record GeneratorRecipe(ItemGenerator itemGenerator) {

    public ItemStack getGeneratorItemStack() {
        ItemStack itemStack = new ItemStack(Material.FURNACE, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(ColorUtils.colorize("&eItem Generator"));
        itemMeta.setLore(List.of(
                ColorUtils.colorize("&8&m-----------------"),
                ColorUtils.colorize(""),
                ColorUtils.colorize("&eSneak + Right click to Open the GUI")
        ));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ConfigurationSection getRecipeSection() {
        return itemGenerator.getConfig().getConfigurationSection("Recipe");
    }

    public HashMap<String, Material> getRecipeItems() {
        HashMap<String, Material> items = new HashMap<>();
        ConfigurationSection itemsSection = getRecipeSection().getConfigurationSection("Items");
        itemsSection.getKeys(false).forEach(key -> {
            Material material = Material.getMaterial(itemsSection.getString(key));
            items.put(key, material);
        });
        return items;
    }

    public List<String> getCraftingRecipe() {
        return getRecipeSection().getStringList("Crafting");
    }

    public void createRecipe() {
        NamespacedKey namespacedKey = new NamespacedKey(itemGenerator, "itemGeneratorRecipe");
        ShapedRecipe itemGeneratorRecipe = new ShapedRecipe(namespacedKey, getGeneratorItemStack());

        itemGeneratorRecipe.shape(getCraftingRecipe().toArray(String[]::new));

        getRecipeItems().forEach((key, material) -> {
            itemGeneratorRecipe.setIngredient(key.charAt(0), material);
        });

        itemGenerator.getServer().addRecipe(itemGeneratorRecipe);
    }

    public Inventory getRecipeInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, ColorUtils.colorize("&eItem Generator"));

        ItemStack deco = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setNoName().create();

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, deco);
        }

        String arrowValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDllY2NjNWMxYzc5YWE3ODI2YTE1YTdmNWYxMmZiNDAzMjgxNTdjNTI0MjE2NGJhMmFlZjQ3ZTVkZTlhNWNmYyJ9fX0=";
        ItemStack arrowItem = new ItemBuilder(Material.PLAYER_HEAD).setNoName().setSkull(arrowValue).create();

        HashMap<String, Material> recipeItems = getRecipeItems();
        List<String> craftingRecipe = getCraftingRecipe();

        int slot = 1;

        for (int i = 0; i < craftingRecipe.size(); i++) {
            String[] keys = craftingRecipe.get(i).split("");
            for (int y = 0; y < keys.length; y++) {
                String key = keys[y];
                Material material = recipeItems.get(key);
                inventory.setItem(slot, new ItemBuilder(material).setNoName().create());
                slot++;
            }
            slot += 6;
        }

        inventory.setItem(9 + 5, arrowItem);
        inventory.setItem(9 + 7, getGeneratorItemStack());

        return inventory;
    }

}
