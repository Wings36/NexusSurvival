package nexusSurvival.homeTeleporter;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class admintest implements CommandExecutor {

    Plugin plugin;

    public admintest(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location location = commandSender.getServer().getPlayer(commandSender.getName()).getLocation();
        teleportPlayer(commandSender.getServer().getPlayer(commandSender.getName()), location);
        commandSender.sendMessage(String.valueOf(location.getY()));
        return true;
    }
    private void teleportPlayer(Player player, Location location) {
        Location setlocation = new Location(plugin.getServer().getWorld("world"), location.getX(), location.getY(), location.getZ());
        double rateCircleOneGrow = 15.0 / 20.0;
        double rateCircleTwoGrow = 7.0 / 20.0;
        double rateCircleThreeGrow = 3.0 / 20.0;

        BukkitScheduler bukkitscheduler = Bukkit.getScheduler();
        BukkitTask teleportCylinder = bukkitscheduler.runTaskTimer(plugin, () -> {
            createCylinder(setlocation, 1, 1);
            createCylinder(player.getLocation(), 1, 1);
        }, 1L , 1L);
        var ref = new Object() {
            double radius = 0.1;
            double radiusTwo = 0.1;
            double radiusThree = 0.1;
        };
        BukkitTask summonCircleTwo = bukkitscheduler.runTaskTimer(plugin, () -> {
            Location setLocation = setlocation;
            drawCircle(setLocation, ref.radius);
            drawCircle(setLocation, ref.radiusTwo);
            drawCircle(setLocation, ref.radiusThree);
            ref.radius = ref.radius + rateCircleOneGrow;
            ref.radiusTwo = ref.radiusTwo + rateCircleTwoGrow;
            ref.radiusThree = ref.radiusThree + rateCircleThreeGrow;

        }, 3L , 1L);

        BukkitTask endCylinder = bukkitscheduler.runTaskLater(plugin, () -> {
            bukkitscheduler.cancelTask(teleportCylinder.getTaskId());
        }, 5L); //3 tick delay
        BukkitTask teleport = bukkitscheduler.runTaskLater(plugin, () -> {
            player.teleport(setlocation);
        }, 2L); //3 tick delay
        BukkitTask endFour = bukkitscheduler.runTaskLater(plugin, () -> {
            bukkitscheduler.cancelTask(summonCircleTwo.getTaskId());
        }, 40L); //0.5 second delay
    }
    public void createCylinder(Location location, double height, double radius) {
        Location setLocation = location.clone();
        drawCircle(setLocation, 0);
        for(double x = 0; x <= height; x = x + 0.5) {
            drawCircle(setLocation, radius);
            setLocation.setY(setLocation.getY() + 0.5);
        }
        drawCircle(setLocation, 0);
    }

    public void drawCircle(Location location, double radius) {
        double xCurrent = location.getX();
        double zCurrent = location.getZ();
        double yCurrent = location.getY();
        World world = location.getWorld();
        final double pi = Math.PI * 2;
        plugin.getLogger().info(String.valueOf(yCurrent));
        for (double theta = 0.0; theta <= pi; theta += 0.1) {
            double z = (Math.cos(theta) * radius) + zCurrent;
            double x = (Math.sin(theta) * radius) + xCurrent;
            world.spawnParticle(Particle.COMPOSTER, x, yCurrent, z, 20, 0.2, 0.2 ,0.2);

        }

    }
}
