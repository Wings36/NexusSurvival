package nexusSurvival.homeTeleporter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpawnSphere implements CommandExecutor {
    TeleporterClock clock;

    public SpawnSphere(TeleporterClock clock) {
        this.clock = clock;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length >=1) {
            commandSender.sendMessage("'"+strings[0]+"'");
            clock.createCylinder(commandSender.getServer().getPlayer(commandSender.getName()).getLocation(), Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));
            return true;
        }
        return false;
    }
}
