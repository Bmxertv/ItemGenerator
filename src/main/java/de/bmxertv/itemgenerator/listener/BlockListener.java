package de.bmxertv.itemgenerator.listener;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.menu.GeneratorMenu;
import de.bmxertv.itemgenerator.model.GeneratorModel;
import de.bmxertv.itemgenerator.util.ColorUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public record BlockListener(ItemGenerator itemGenerator) implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getItemInHand().hasItemMeta() || event.getItemInHand().getItemMeta() == null) return;


        if (!event.getItemInHand().getType().equals(Material.FURNACE)) return;
        if (!event.getItemInHand().getItemMeta().getDisplayName().equals(ColorUtils.colorize("&eItem Generator")))
            return;
        if (!event.getItemInHand().getItemMeta().hasLore()) return;

        itemGenerator.getGeneratorConfig().save(event.getBlockPlaced());

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.FURNACE)) return;
        if (!itemGenerator.getGeneratorConfig().isGenerator(event.getBlock())) return;

        itemGenerator.getGeneratorConfig().delete(event.getBlock());
        event.setDropItems(false);
        event.getPlayer().getInventory().addItem(itemGenerator.getGeneratorRecipe().getGeneratorItemStack());
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if (!clickedBlock.getType().equals(Material.FURNACE)) return;

        if (!itemGenerator.getGeneratorConfig().isGenerator(clickedBlock)) return;

        event.setCancelled(true);
        event.setUseInteractedBlock(Event.Result.DENY);

        GeneratorModel generatorModel = itemGenerator.getGeneratorConfig().getGeneratorModel(clickedBlock);

        GeneratorMenu menu = new GeneratorMenu(generatorModel);
        menu.openInventory(player);
    }

}
