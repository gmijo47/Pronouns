package io.gmijo;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class PronounsCommand implements CommandExecutor {

    private Pronouns plugin;
    private Pattern validPronounPattern;

    public PronounsCommand(Pronouns plugin) {
        this.plugin = plugin;
        // Pattern to check if the pronouns are one word, all lowercase, and without numbers
        validPronounPattern = Pattern.compile("^[a-z]+$");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        if (!sender.hasPermission("pronouns.add")) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length != 2 || !args[0].equalsIgnoreCase("add")) {
            player.sendMessage("Usage: /pronouns add <pronoun>");
            return true;
        }

        String pronoun = args[1].toLowerCase(); // Convert to lowercase
        if (!isValidPronoun(pronoun)) {
            player.sendMessage("Invalid pronoun. Pronouns should be one word, all lowercase, and without numbers.");
            return true;
        }

        plugin.getPronounAssociations().put(player.getUniqueId(), pronoun);
        plugin.saveAssociations();
        plugin.spawnArmorStand(player, pronoun); // Spawn armor stand when pronouns are set
        player.sendMessage("Your pronouns have been set to: " + pronoun);

        return true;
    }

    private boolean isValidPronoun(String pronoun) {
        return validPronounPattern.matcher(pronoun).matches();
    }
}
