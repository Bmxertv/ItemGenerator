package de.bmxertv.itemgenerator.listener;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.menu.GeneratorMenu;
import de.bmxertv.itemgenerator.model.GeneratorModel;
import de.bmxertv.itemgenerator.util.ColorUtils;
import de.bmxertv.itemgenerator.util.GeneratorConfig;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public record InventoryListener(ItemGenerator itemGenerator) implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String stripTitle = ColorUtils.strip(event.getView().getTitle());
        if (!stripTitle.contains("Item Generator")) return;
        event.setCancelled(true);

        int id = Integer.parseInt(stripTitle.replace("Item Generator [", "").replace("]", ""));

        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getType() == Material.AIR) return;
        if (event.getCurrentItem().getItemMeta() == null) return;

        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 10: // Full
                Inventory playerInventory = player.getInventory();
                int slot = 0;
                int full = 0;
                boolean updateAllow = false;
                if (playerInventory.contains(Material.COAL)) {
                    slot = playerInventory.first(Material.COAL);
                    full = 1;
                    updateAllow = true;
                } else if (playerInventory.contains(Material.COAL_BLOCK)) {
                    slot = playerInventory.first(Material.COAL_BLOCK);
                    full = 9;
                    updateAllow = true;
                }

                if (!updateAllow) {
                    player.sendMessage(ColorUtils.colorize("%s You can not refill full because you have no coal in your inventory".formatted(itemGenerator.PREFIX)));
                    return;
                }
                ItemStack fullItem = playerInventory.getItem(slot);
                fullItem.setAmount(fullItem.getAmount() - 1);
                playerInventory.setItem(slot, fullItem);
                updateFull(player, id, full);
                break;
            case 13: // Item
                ItemStack itemInHand = player.getInventory().getItemInMainHand();
                if (itemInHand == null) {
                    player.sendMessage(ColorUtils.colorize("%s You must hold a Item in your main hand".formatted(itemGenerator.PREFIX)));
                    return;
                }

                if (!itemGenerator.getConfig().getStringList("AllowItems").contains(itemInHand.getType().getKey().getKey().toUpperCase())) {
                    player.sendMessage(ColorUtils.colorize("%s You must not let this item generate".formatted(itemGenerator.PREFIX)));
                    return;
                }

                updateItem(player, id, itemInHand);

                break;
            case 16: // GeneratedItem
                giveOutput(player, id);
                break;
        }

    }

    private void updateFull(Player player, int id, int full) {
        itemGenerator.getGeneratorConfig().addFull(id, full);
        new GeneratorMenu(itemGenerator.getGeneratorConfig().getGeneratorModel(id)).update(player);
    }

    private void updateItem(Player player, int id, ItemStack item) {
        itemGenerator.getGeneratorConfig().setItem(id, item);
        new GeneratorMenu(itemGenerator.getGeneratorConfig().getGeneratorModel(id)).update(player);
    }

    private void giveOutput(Player player, int id) {
        GeneratorConfig generatorConfig = itemGenerator.getGeneratorConfig();
        GeneratorModel generatorModel = generatorConfig.getGeneratorModel(id);
        ItemStack output = generatorModel.getItem();
        output.setAmount(generatorModel.getGenerated());
        player.getInventory().addItem(output);
        generatorConfig.setGenerated(id, 0);
        new GeneratorMenu(itemGenerator.getGeneratorConfig().getGeneratorModel(id)).update(player);
    }

}
