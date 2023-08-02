package io.gmijo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnExtremeArmorStandCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnextremearmorstand")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                // Spawn a small armor stand at the player's location
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);

                // Customize the armor stand
                armorStand.setSmall(true);
                armorStand.setCustomName("extreme");
                armorStand.setCustomNameVisible(true);
                armorStand.setInvisible(true);

                // Set the player as a passenger of the armor stand
                player.addPassenger(armorStand);

                sender.sendMessage("Spawned a small armor stand with the tag 'extreme' and set you as a passenger.");
                return true;
            } else {
                sender.sendMessage("Only players can execute this command.");
                return false;
            }
        }
        return false;
    }
}
