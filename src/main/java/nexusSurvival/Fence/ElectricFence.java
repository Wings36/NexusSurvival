package nexusSurvival.Fence;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class ElectricFence implements Listener {
    Plugin plugin;

    public void accessPlugin() {
        plugin.getServer();
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
            Player player = event.getPlayer();
            if (playerTest(player)) {
                int x1 = (int) Math.floor(event.getFrom().getX());
                int z1 = (int) Math.floor(event.getFrom().getZ());
                int x2 = (int) Math.floor(event.getTo().getX());
                int z2 = (int) Math.floor(event.getTo().getZ());

                if (x1 != x2 || z1 != z2) {
                    if (breachTest(player.getLocation())) {
                        player.getWorld().strikeLightning(player.getLocation());
                    }
                }
            }
    }
    private boolean breachTest(Location location) {
        if (location.getX() <= 9872 || location.getX() >= 10825 || location.getZ() <= 4056 || location.getZ() >= 5009) {
            return true;
        }
        return false;
    }
    private boolean playerTest(Player player) {
        if(!plugin.getConfig().isSet("fence.Players." + player.getName() + ".status")) {
            return true;
        }
        return false;
    }


}
