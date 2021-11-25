package nexusSurvival.homeTeleporter;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TpLookup implements CommandExecutor {

    TeleporterClock clock;
    Plugin plugin;

    public TpLookup(TeleporterClock clock, Plugin plugin) {
        this.clock = clock;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            if (strings.length >= 1) {
                if (plugin.getServer().getPlayer(strings[0]) != null) {
                    Player player = plugin.getServer().getPlayer(strings[0]);
                    commandSender.sendMessage("Player " + player.getName() + "'s TP location is " +
                            clock.getPlayerX(player) + " " +
                            clock.getPlayerY(player) + " " +
                            clock.getPlayerZ(player));
                    return true;
                }
            }
        }
        else {
            commandSender.sendMessage("You do not have permission!");
            return true;
        }
        return false;
    }
}
