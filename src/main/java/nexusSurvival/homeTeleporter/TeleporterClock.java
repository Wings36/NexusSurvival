package nexusSurvival.homeTeleporter;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class TeleporterClock implements Listener {

    Plugin plugin;
    ItemStack clock;

    HashMap<Player, Location> destinations = new HashMap<Player, Location>();
    HashMap<Player, int[]> bukkitScheduleList = new HashMap<Player, int[]>();

    public void createCylinder(Location location, double height, double radius) {

        drawCircle(location, 0);
        for(double x = 0; x <= height; x = x + 0.5) {
            drawCircle(location, radius);
            location.setY(location.getY() + 0.5);
        }
        drawCircle(location, 0);
    }

    //set to private later
    public void tpAni(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        double playerX = player.getLocation().getX();
        double playerZ = player.getLocation().getZ();
        double playerY = player.getLocation().getY();
        double rateAscent = Math.abs((256.0 - playerY) / 30);

        final double circleOne = 15;
        final double circleTwo = 7;
        final double circleThree = 3;

        double rateCircleOneGrow = circleOne / 20;
        double rateCircleTwoGrow = circleTwo / 20;
        double rateCircleThreeGrow = circleThree / 20;

        double rateCircleOneShrink = circleOne / 200;
        double rateCircleTwoShrink = circleTwo / 200;
        double rateCircleThreeShrink = circleThree / 200;

        var ref = new Object() {
            double radius = 0.0;
            double radiusTwo = 0.0;
            double radiusThree = 0.0;
            double height = playerY;
        };
        //
        BukkitScheduler bukkitscheduler = Bukkit.getScheduler();

        BukkitTask groundCircleTask = bukkitscheduler.runTaskTimer(plugin, () -> {
            drawCircle(player.getLocation(), ref.radius);
            drawCircle(player.getLocation(), ref.radiusTwo);
            drawCircle(player.getLocation(), ref.radiusThree);
            ref.radius = ref.radius - rateCircleOneShrink;
            ref.radiusTwo = ref.radiusTwo - rateCircleTwoShrink;
            ref.radiusThree = ref.radiusThree - rateCircleThreeShrink;

        }, 20L , 1L);

        BukkitTask summonCircle = bukkitscheduler.runTaskTimer(plugin, () -> {
            drawCircle(player.getLocation(), ref.radius);
            drawCircle(player.getLocation(), ref.radiusTwo);
            drawCircle(player.getLocation(), ref.radiusThree);
            ref.radius = ref.radius + rateCircleOneGrow;
            ref.radiusTwo = ref.radiusTwo + rateCircleTwoGrow;
            ref.radiusThree = ref.radiusThree + rateCircleThreeGrow;

        }, 1L , 1L);

        BukkitTask summonCircleTwo = bukkitscheduler.runTaskTimer(plugin, () -> {
            drawCircle(player.getLocation(), ref.radius);
            drawCircle(player.getLocation(), ref.radiusTwo);
            drawCircle(player.getLocation(), ref.radiusThree);
            ref.radius = ref.radius + rateCircleOneGrow;
            ref.radiusTwo = ref.radiusTwo + rateCircleTwoGrow;
            ref.radiusThree = ref.radiusThree + rateCircleThreeGrow;

        }, 200L , 1L);

        BukkitTask skyBeam = bukkitscheduler.runTaskTimer(plugin, () -> {
            for(double spawnHeight = playerY + 2; spawnHeight <= ref.height; spawnHeight = spawnHeight + 0.1) {
                world.spawnParticle(Particle.COMPOSTER, playerX, spawnHeight, playerZ, 1);
            }
            if (ref.height < 256) {
                ref.height = ref.height + rateAscent;
            }
        }, 1L , 1L);

        BukkitTask teleportCylinder = bukkitscheduler.runTaskTimer(plugin, () -> {
            createCylinder(location, 1, 1);
            createCylinder(player.getLocation(), 1, 1);
        }, 196L , 1L);

        int taskSkyBeam = skyBeam.getTaskId();
        int taskGroundCircle = groundCircleTask.getTaskId();
        int taskTeleportCylinder = teleportCylinder.getTaskId();
        int taskSummonCircle = summonCircle.getTaskId();
        int taskSummonCircleTwo = summonCircleTwo.getTaskId();

        //end
        BukkitTask endOne = bukkitscheduler.runTaskLater(plugin, () -> {
            bukkitscheduler.cancelTask(taskSkyBeam);
            bukkitscheduler.cancelTask(taskGroundCircle);
            bukkitscheduler.cancelTask(taskTeleportCylinder);
        }, 200L); //10 second delay


        BukkitTask endTwo = bukkitscheduler.runTaskLater(plugin, () -> {
            bukkitscheduler.cancelTask(taskSummonCircle);
        }, 20L); //1 second delay

        BukkitTask endThree = bukkitscheduler.runTaskLater(plugin, () -> {
            player.teleport(destinations.get(player));
        }, 198L);

        BukkitTask endFour = bukkitscheduler.runTaskLater(plugin, () -> {
            bukkitscheduler.cancelTask(taskSummonCircleTwo);
        }, 210L); //0.5 second delay

        int taskEndOne = endOne.getTaskId();
        int taskEndTwo = endTwo.getTaskId();
        int taskEndThree = endThree.getTaskId();
        int taskEndFour = endFour.getTaskId();

        int[] playerTasks = {taskSkyBeam, taskGroundCircle, taskTeleportCylinder, taskSummonCircle, taskSummonCircleTwo, taskEndOne, taskEndTwo, taskEndThree, taskEndFour};
        bukkitScheduleList.put(player, playerTasks);
    }

    public void resetTP(Player player){
        if (destinations.containsKey(player)) {
            destinations.remove(player);
            plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".status", false);
            player.sendMessage("Successfully Reset Recall Clock");
        }
        else {
            player.sendMessage("Error: Recall Clock Not Set");
        }
    }

    public TeleporterClock (Plugin plugin) {
        this.plugin = plugin;
        clock = new ItemStack(Material.CLOCK);
        setupClock();
    }

    @EventHandler
    public void interruptTPEvent(PlayerMoveEvent event) {
        double x1 = event.getFrom().getX();
        double y1 = event.getFrom().getY();
        double z1 = event.getFrom().getZ();

        double x2 = event.getTo().getX();
        double y2 = event.getTo().getY();
        double z2 = event.getTo().getZ();

        if (x1 != x2 || y1 != y2 || z1 != z2) { interruptTP(event.getPlayer()); }
    }

    @EventHandler
    public void interruptTPEvent(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().equals(clock)) {
            if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock() != ) {
                interruptTP(event.getPlayer());
            }
        }
    }

    private void interruptTP(Player player) {
        if (bukkitScheduleList.containsKey(player)) {
            BukkitScheduler bukkitscheduler = Bukkit.getScheduler();
            int[] playerTasks = bukkitScheduleList.get(player).clone();
            for (int x = 0; x <= 8; x++) {
                bukkitscheduler.cancelTask(playerTasks[x]);
            }
            bukkitScheduleList.remove(player);
            player.sendMessage("Teleport Cancelled");
        }
    }



    public ItemStack getClock() { return clock.clone(); }

    @EventHandler
    public void clickItem(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand().equals(clock)) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(!(bukkitScheduleList.containsKey(event.getPlayer()))) {
                    Player player = event.getPlayer();
                    if (destinations.containsKey(player)) {
                        event.getPlayer().sendMessage("Teleporting...");
                        tpAni(player);
                    } else {
                        addLocation(event.getPlayer());
                        event.getPlayer().sendMessage("Teleport Location Set");
                    }
                }
            }
        }
    }
    public void drawCircle(Location location, double radius) {
        double xCurrent = location.getX();
        double zCurrent = location.getZ();
        double yCurrent = location.getY();
        World world = location.getWorld();
        final double pi = 3.14159265 * 2;

        for (double theta = 0.0; theta <= pi; theta += 0.1) {
            double z = (Math.cos(theta) * radius) + zCurrent;
            double x = (Math.sin(theta) * radius) + xCurrent;
            world.spawnParticle(Particle.COMPOSTER, x, yCurrent, z, 20, 0.2, 0.2 ,0.2);

        }
    }

    private void addLocation(Player player) {
        destinations.put(player, player.getLocation());
        Location location = player.getLocation();
        plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".status", true);
        plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".world", location.getWorld().getName());
        plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".X", location.getX());
        plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".Y", location.getY());
        plugin.getConfig().set("ClockTeleport.Players." + player.getName() + ".Z", location.getZ());
        plugin.saveConfig();
        logLocation(location, player);
    }
    public void setLocation(Location location, Player player) { destinations.put(player, location); logLocation(location, player);}

    private void setupClock() {
        ItemMeta meta = clock.getItemMeta();
        meta.setDisplayName("Recall Clock");
        meta.setUnbreakable(true);
    }

    private void logLocation(Location location, Player player) {
        plugin.getLogger().info("Set " + player.getName() + "tp location to " + location.getX() + " " + location.getY() + " " +location.getZ());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!(destinations.containsKey(event.getPlayer()))) {
            if(plugin.getConfig().getBoolean("ClockTeleport.Players." + event.getPlayer().getName() + ".status")) {
                Player player = event.getPlayer();
                World world = plugin.getServer().getWorld(plugin.getConfig().getString("ClockTeleport.Players." + player.getName() + ".world"));
                double x = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".X");
                double y = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".Y");
                double z = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".Z");
                Location location = new Location(world, x, y ,z);
                destinations.put(player,location);
            }
        }
    }

    public void reloadPlayer(Player player) {
            if(plugin.getConfig().getBoolean("ClockTeleport.Players." + player.getName() + ".status")) {
                World world = plugin.getServer().getWorld(plugin.getConfig().getString("ClockTeleport.Players." + player.getName() + ".world"));
                double x = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".X");
                double y = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".Y");
                double z = plugin.getConfig().getDouble("ClockTeleport.Players." + player.getName() + ".Z");
                Location location = new Location(world, x, y ,z);
                destinations.put(player,location);
                player.sendMessage("Successfully Reloaded Last Saved Location");
            }
            else {
                player.sendMessage("Error. Cannot load last saved location.");
            }
    }

    //Return player data

    public double getPlayerX(Player player) {
        Location location = destinations.get(player);
        return location.getX();
    }
    public double getPlayerY(Player player) {
        Location location = destinations.get(player);
        return location.getY();
    }
    public double getPlayerZ(Player player) {
        Location location = destinations.get(player);
        return location.getZ();
    }
}
