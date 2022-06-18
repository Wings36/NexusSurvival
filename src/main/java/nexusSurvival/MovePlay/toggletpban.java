package nexusSurvival.MovePlay;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class toggletpban implements CommandExecutor {
    Plugin plugin;

    public toggletpban(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (plugin.getConfig().getBoolean("worldtp")) {
            plugin.getConfig().set("worldtp", false);
            commandSender.sendMessage("Set tp to false");
        }
        else {
            plugin.getConfig().set("worldtp", true);
            commandSender.sendMessage("Set tp to true");
        }
        plugin.saveConfig();
        return true;
    }
}
