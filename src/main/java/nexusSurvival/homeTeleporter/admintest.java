package nexusSurvival.homeTeleporter;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class admintest implements CommandExecutor {

    TeleporterClock clock;

    public admintest(TeleporterClock clock) {
        this.clock = clock;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = commandSender.getServer().getPlayer(commandSender.getName());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent( "Casting... 0.0s: ======================="));
        return true;
    }
}
