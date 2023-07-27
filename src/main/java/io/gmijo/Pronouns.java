package io.gmijo;
import com.lkeehl.tagapi.TagAPI;
import com.lkeehl.tagapi.TagBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public class Pronouns extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the command executor
        getCommand("prounounce").setExecutor(new PronounceCommandExecutor());
        TagAPI.onEnable(this);
    }

    // Command executor class
    public class PronounceCommandExecutor implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (cmd.getName().equalsIgnoreCase("prounounce")) {
                if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
                    String playerName = args[1];
                    Player player = getServer().getPlayer(playerName);
                    if (player != null) {

                        TagBuilder builder = TagBuilder.create(player);
                        builder.withLine(pl->args[2]);
                        builder.build();

                        player.setPlayerListName("[" + args[2] + "]" + playerName);
                        sender.sendMessage("Set the text below " + playerName + "'s nametag.");
                    } else {
                        sender.sendMessage("Player not found.");
                    }
                    return true;
                }
            }
            return false;
        }
    }
}