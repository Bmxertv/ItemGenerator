package de.bmxertv.itemgenerator.command;

import de.bmxertv.itemgenerator.ItemGenerator;
import de.bmxertv.itemgenerator.util.ConsoleUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public record RecipeCommand(ItemGenerator itemGenerator) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            ConsoleUtil.info("%s You must be a Player to use this Command!".formatted(itemGenerator.CONSOLE_PREFIX));
            return true;
        }

        Player player = (Player) sender;

        player.openInventory(itemGenerator.getGeneratorRecipe().getRecipeInventory());

        return true;
    }
}
