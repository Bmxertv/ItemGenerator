package de.bmxertv.itemgenerator.menu;

import de.bmxertv.itemgenerator.model.GeneratorModel;
import de.bmxertv.itemgenerator.util.ColorUtils;
import de.bmxertv.itemgenerator.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GeneratorMenu {

    private GeneratorModel model;
    private Inventory inventory;

    public GeneratorMenu(GeneratorModel model) {
        this.model = model;
    }

    public void createInventory() {
        this.inventory = Bukkit.createInventory(null, 9 * 3, ColorUtils.colorize("&eItem Generator &8[%s]".formatted(model.getId())));

        ItemStack deco = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setNoName().create();

        for (int i = 0; i < this.inventory.getSize(); i++) {
            this.inventory.setItem(i, deco);
        }

        int slot = 6;
        ItemStack decoOutput = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setNoName().create();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                inventory.setItem(slot, decoOutput);
                slot++;
            }
            slot += 6;
        }

        ItemStack full = new ItemBuilder(Material.COAL)
                .setName("&7Full &8[&e%s&8]".formatted(this.model.getFull()))
                .setLore("&7&m---------",
                        "",
                        "&7Use the coal in your",
                        "&7inventory to fill the generator")
                .create();

        ItemStack output = this.model.getItem().clone();
        output.setAmount(this.model.getGenerated());

        this.inventory.setItem(8 + 2, full);
        this.inventory.setItem(8 + 5, this.model.getItem());
        this.inventory.setItem(8 + 8, output);

    }

    public void openInventory(Player player) {
        createInventory();
        player.openInventory(inventory);
    }

    public void update(Player player) {
        createInventory();
        openInventory(player);
    }
}
