package nexusSurvival.homeTeleporter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class ResetTp implements CommandExecutor {

    TeleporterClock clock;
    Plugin plugin;

    public ResetTp(TeleporterClock clock, Plugin plugin) {
        this.clock = clock;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            if(strings.length == 2) {
                Player player = plugin.getServer().getPlayer(strings[1]);
                if (player != null) {
                    clock.resetTP(player);
                    return true;
                }
                else {
                    clock.resetTP(commandSender.getServer().getPlayer(commandSender.getName()));
                    return true;
                }
            }
            else {
                clock.resetTP(commandSender.getServer().getPlayer(commandSender.getName()));
                return true;
            }
        }
        else {
            clock.resetTP(commandSender.getServer().getPlayer(commandSender.getName()));
            return true;
        }
    }
}
