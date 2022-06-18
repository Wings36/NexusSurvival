package nexusSurvival.MovePlay;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class tpStory implements CommandExecutor {

    Plugin plugin;
    public tpStory(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //dabd and kuro shit
        Location location = new Location(plugin.getServer().getWorld("world"), 10603.00,66.00,4571.00);
        plugin.getServer().getPlayer("DaBDslayer").teleport(location);
        plugin.getServer().getPlayer("CorKuro").teleport(location);
        return false;
    }
}
