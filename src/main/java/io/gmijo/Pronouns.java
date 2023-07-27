package io.gmijo;
import com.lkeehl.tagapi.TagAPI;
import com.lkeehl.tagapi.TagBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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
                    if (sender != null && sender instanceof Player) {

                        TagBuilder builder = TagBuilder.create((Entity) sender);
                        builder.withLine(pl->args[1]);
                        builder.build();


                        sender.sendMessage("Set the text below " + sender + "'s nametag.");
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