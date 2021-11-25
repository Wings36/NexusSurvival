package nexusSurvival.homeTeleporter;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ClockTPReplace implements CommandExecutor {

    public ClockTPReplace(Plugin plugin, TeleporterClock clock) {
        this.plugin = plugin;
        this.clock = clock;
    }

    Plugin plugin;
    TeleporterClock clock;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.isOp()) {
            if (strings.length >= 4) {
                if (plugin.getServer().getPlayer(strings[0]) != null) {
                    Player player = plugin.getServer().getPlayer(strings[0]);
                    double x = Double.parseDouble(strings[1]);
                    double y = Double.parseDouble(strings[2]);
                    double z = Double.parseDouble(strings[3]);
                    Location location = new Location(player.getWorld(), x, y ,z);
                    clock.setLocation(location, player);
                    commandSender.sendMessage("Successfully set player " + player.getName() + " to " +
                            clock.getPlayerX(player) + " " +
                            clock.getPlayerY(player) + " " +
                            clock.getPlayerZ(player));
                    return true;
                }
                else {
                    commandSender.sendMessage("Error cannot find player");
                    return true;
                }

            } else {
                return false;
            }
        }
        commandSender.sendMessage("You do not have permission");
        return true;
    }
}
