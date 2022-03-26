package nexusSurvival.MovePlay;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.util.Iterator;

public class LoginEvent implements Listener {
    Plugin plugin;

    public LoginEvent(Plugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void PlayerLoginEvent(PlayerJoinEvent event) {
        if (plugin.getConfig().getBoolean("resetevent.active")) {
            Player player = event.getPlayer();
            if(!plugin.getConfig().isSet("resetevent.Players." + event.getPlayer().getName() + ".status")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke " + player.getName() + " everything");
                plugin.getConfig().set("resetevent.Players." + event.getPlayer().getName() + ".status", true);
                plugin.saveConfig();
            }
        }

    }
}