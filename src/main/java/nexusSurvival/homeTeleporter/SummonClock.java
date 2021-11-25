package nexusSurvival.homeTeleporter;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SummonClock implements CommandExecutor {
    TeleporterClock clock;
    Plugin plugin;

    public SummonClock(Plugin plug, TeleporterClock clock) {
        this.clock = clock;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command lightning, String label, String[] args) {
        if (sender.isOp()) {
                sender.getServer().getPlayer(sender.getName()).getInventory().addItem(clock.getClock());
                sender.sendMessage("Gave you one Recall Clock!");
                return true;
        }
        else {
            sender.sendMessage("You do not have permission");
            return true;
        }

    }

}

