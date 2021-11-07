package de.bmxertv.itemgenerator.listener;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.util.ColorUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public record InventoryListener(ItemGenerator itemGenerator) implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String stripTitle = ColorUtils.strip(event.getView().getTitle());
        if (!stripTitle.contains("Item Generator")) return;
        event.setCancelled(true);

        String id = stripTitle.replace("Item Generator [", "").replace("]", "");
        System.out.println(id);
    }

}
