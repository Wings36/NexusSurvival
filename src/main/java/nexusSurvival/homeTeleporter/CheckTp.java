package nexusSurvival.homeTeleporter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckTp implements CommandExecutor {
    TeleporterClock clock;

    public CheckTp(TeleporterClock clock) { this.clock = clock; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        clock.reloadPlayer(commandSender.getServer().getPlayer(commandSender.getName()));
        return true;
    }
}
