package nexusSurvival.MovePlay;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class netherban implements Listener {

    Plugin plugin;
    public netherban(Plugin plugin) {this.plugin = plugin;}
    @EventHandler
    public void PlayerTPEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp()) {
            if(plugin.getConfig().getBoolean("worldtp")) {
                if(event.getTo().getWorld().equals(plugin.getServer().getWorld("world_nether")) && event.getFrom().equals(plugin.getServer().getWorld("world"))) {
                    if (player.getName().equals("Dabdslayer") || player.getName().equals("CorKuro")) {
                        event.setCancelled(true);
                        player.sendMessage("Error. World Teleporting Disabled");
                    }

                }
            }
        }
    }

    @EventHandler
    public void PlayerNetherPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp()) {
            if(plugin.getConfig().getBoolean("worldtp")) {
                event.setCancelled(true);
                player.sendMessage("Error. World Teleportation Disabled");
            }
        }
    }
}
