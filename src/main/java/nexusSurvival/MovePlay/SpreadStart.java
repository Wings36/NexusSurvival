package nexusSurvival.MovePlay;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SpreadStart implements CommandExecutor {

    Plugin plugin;
    public SpreadStart(Plugin plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()) {
            BukkitScheduler bukkitscheduler = Bukkit.getScheduler();
            Spread spread = new Spread(plugin);
            List<Player> players = plugin.getServer().getWorld("world").getPlayers();
            World world = plugin.getServer().getWorld("world");
            HashMap<Player, PlayerInfo> playerinfo = new HashMap<Player, PlayerInfo>();

            world.getWorldBorder().reset();

            commandSender.sendMessage("Sending...");
            for (Player player : players) {
                double height = player.getLocation().getY();
                playerinfo.put(player, new PlayerInfo(height));
                commandSender.sendMessage(player.getName());
            }
            var ref = new Object() {
                int timer = 30;
            };
            BukkitTask skyBeam = bukkitscheduler.runTaskTimer(plugin, () -> {
                for (Player player : players) {
                    PlayerInfo playerInfoSelect = playerinfo.get(player);
                    double playerY = player.getLocation().getY();
                    double playerX = player.getLocation().getX();
                    double playerZ = player.getLocation().getZ();
                    double rateAscent = playerInfoSelect.rateAscent;

                    for (double spawnHeight = playerY + 2; spawnHeight <= playerInfoSelect.height; spawnHeight = spawnHeight + 0.1) {
                        world.spawnParticle(Particle.COMPOSTER, playerX, spawnHeight, playerZ, 1);
                    }
                    if (playerInfoSelect.height < 256) {
                        playerInfoSelect.height = playerInfoSelect.height + rateAscent;
                    }
                }
            }, 1L , 1L);
            BukkitTask countdown = bukkitscheduler.runTaskTimer(plugin, () -> {
                for (Player player : players) {
                    player.sendTitle("Warping...", String.valueOf(ref.timer), 10, 20, 20);
                }
                ref.timer = ref.timer - 1;
            }, 0 , 20L);

            BukkitTask spreadPlayers = bukkitscheduler.runTaskLater(plugin, () -> {
                spread.run();
                bukkitscheduler.cancelTask(countdown.getTaskId());
                plugin.getServer().getConsoleSender().sendMessage("GO");
                for(Player player : players) {
                    player.getInventory().clear();
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke " + player.getName() + " everything");
                    player.setExp(0);
                    plugin.getConfig().set("resetevent.Players." + player.getName() + ".status", true);
                }
                plugin.getConfig().set("resetevent.active", true);
                plugin.saveConfig();
            }, 600L);
            BukkitTask worldborder = bukkitscheduler.runTaskLater(plugin, () -> {
                Location WBnew = new Location(plugin.getServer().getWorld("world"), 9000, 64, 9000);
                world.getWorldBorder().setCenter(WBnew);
                world.getWorldBorder().setSize(10000);
            }, 800L);



            int taskSkyBeam = skyBeam.getTaskId();
            BukkitTask endOne = bukkitscheduler.runTaskLater(plugin, () -> {
                bukkitscheduler.cancelTask(taskSkyBeam);
            }, 615L);

            BukkitTask potion = bukkitscheduler.runTaskLater(plugin, () -> {
                PotionEffect effect = new  PotionEffect(PotionEffectType.CONFUSION, 260, 1);
                for (Player player : players) {
                    player.addPotionEffect(effect);
                }
            }, 400L);


            return true;
        }
        return false;
    }

}
