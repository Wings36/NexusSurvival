package nexusSurvival.MovePlay;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;


public class Spread {
    private static final Random random = new Random();
    Plugin plugin;

    public Spread(Plugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        teleportSpread();
    }

    public void teleportSpread() {

        final double x = 9000.000;
        final double z = 9000.000;
        final double distance = 350.000;
        final double range = 2000.000;
        boolean teams = false;



        List<Player> players = plugin.getServer().getWorld("world").getPlayers();
        //exclude dabdslayer and corkuro
        players.remove(plugin.getServer().getPlayer("DaBDslayer"));
        players.remove(plugin.getServer().getPlayer("CorKuro"));



        World world = plugin.getServer().getWorld("world");


        final double xRangeMin = x - range;
        final double zRangeMin = z - range;
        final double xRangeMax = x + range;
        final double zRangeMax = z + range;

        final int spreadSize = teams ? getTeams(players) : players.size();

        final Location[] locations = getSpreadLocations(world, spreadSize, xRangeMin, zRangeMin, xRangeMax, zRangeMax);
        final int rangeSpread = range(world, distance, xRangeMin, zRangeMin, xRangeMax, zRangeMax, locations);

        if (rangeSpread == -1) {
            sendOp(String.format("Could not spread %d %s around %s,%s (too many players for space - try using spread of at most %s)", spreadSize, teams ? "teams" : "players", x, z));
            return;
        }

        final double distanceSpread = spread(world, players, locations, teams);

        sendOp(String.format("Succesfully spread %d %s around %s,%s", locations.length, teams ? "teams" : "players", x, z));
        if (locations.length > 1) {
            sendOp(String.format("(Average distance between %s is %s blocks apart after %s iterations)", teams ? "teams" : "players", String.format("%.2f", distanceSpread), rangeSpread));
        }
        return;
    }


    private int range(World world, double distance, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax, Location[] locations) {
        boolean flag = true;
        double max;

        int i;

        for (i = 0; i < 10000 && flag; ++i) {
            flag = false;
            max = Float.MAX_VALUE;

            Location loc1;
            int j;

            for (int k = 0; k < locations.length; ++k) {
                Location loc2 = locations[k];

                j = 0;
                loc1 = new Location(world, 0, 0, 0);

                for (int l = 0; l < locations.length; ++l) {
                    if (k != l) {
                        Location loc3 = locations[l];
                        double dis = loc2.distanceSquared(loc3);

                        max = Math.min(dis, max);
                        if (dis < distance) {
                            ++j;
                            loc1.add(loc3.getX() - loc2.getX(), 0, 0);
                            loc1.add(loc3.getZ() - loc2.getZ(), 0, 0);
                        }
                    }
                }

                if (j > 0) {
                    loc2.setX(loc2.getX() / j);
                    loc2.setZ(loc2.getZ() / j);
                    double d7 = Math.sqrt(loc1.getX() * loc1.getX() + loc1.getZ() * loc1.getZ());

                    if (d7 > 0.0D) {
                        loc1.setX(loc1.getX() / d7);
                        loc2.add(-loc1.getX(), 0, -loc1.getZ());
                    } else {
                        double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
                        loc2.setX(x);
                        loc2.setZ(z);
                    }

                    flag = true;
                }

                boolean swap = false;

                if (loc2.getX() < xRangeMin) {
                    loc2.setX(xRangeMin);
                    swap = true;
                } else if (loc2.getX() > xRangeMax) {
                    loc2.setX(xRangeMax);
                    swap = true;
                }

                if (loc2.getZ() < zRangeMin) {
                    loc2.setZ(zRangeMin);
                    swap = true;
                } else if (loc2.getZ() > zRangeMax) {
                    loc2.setZ(zRangeMax);
                    swap = true;
                }
                if (swap) {
                    flag = true;
                }
            }

            if (!flag) {
                Location[] locs = locations;
                int i1 = locations.length;

                for (j = 0; j < i1; ++j) {
                    loc1 = locs[j];
                    if (world.getHighestBlockYAt(loc1) == 0) {
                        double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
                        double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
                        locations[i] = (new Location(world, x, 0, z));
                        loc1.setX(x);
                        loc1.setZ(z);
                        flag = true;
                    }
                }
            }
        }

        if (i >= 10000) {
            return -1;
        } else {
            return i;
        }
    }

    private double spread(World world, List<Player> list, Location[] locations, boolean teams) {
        double distance = 0.0D;
        int i = 0;
        Map<Team, Location> hashmap = Maps.newHashMap();

        for (int j = 0; j < list.size(); ++j) {
            Player player = list.get(j);
            Location location;

            if (teams) {
                Team team = player.getScoreboard().getPlayerTeam(player);

                if (!hashmap.containsKey(team)) {
                    hashmap.put(team, locations[i++]);
                }

                location = hashmap.get(team);
            } else {
                location = locations[i++];
            }
            teleportPlayer(player, new Location(world, Math.floor(location.getX()) + 0.5D, world.getHighestBlockYAt((int) location.getX(), (int) location.getZ()), Math.floor(location.getZ()) + 0.5D));
            double value = Double.MAX_VALUE;

            for (int k = 0; k < locations.length; ++k) {
                if (location != locations[k]) {
                    double d = location.distanceSquared(locations[k]);
                    value = Math.min(d, value);
                }
            }

            distance += value;
        }

        distance /= list.size();
        return distance;
    }

    private int getTeams(List<Player> players) {
        Set<Team> teams = Sets.newHashSet();

        for (Player player : players) {
            teams.add(player.getScoreboard().getPlayerTeam(player));
        }

        return teams.size();
    }

    private Location[] getSpreadLocations(World world, int size, double xRangeMin, double zRangeMin, double xRangeMax, double zRangeMax) {
        Location[] locations = new Location[size];

        for (int i = 0; i < size; ++i) {
            double x = xRangeMin >= xRangeMax ? xRangeMin : random.nextDouble() * (xRangeMax - xRangeMin) + xRangeMin;
            double z = zRangeMin >= zRangeMax ? zRangeMin : random.nextDouble() * (zRangeMax - zRangeMin) + zRangeMin;
            locations[i] = (new Location(world, x, 0, z));
        }

        return locations;
    }

    private void sendOp(String msg) {
        for (Player ops : plugin.getServer().getOnlinePlayers()) {
            if (ops.isOp()) {
                ops.sendMessage(msg);
            }
        }
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
        for (double theta = 0.0; theta <= pi; theta += 0.1) {
            double z = (Math.cos(theta) * radius) + zCurrent;
            double x = (Math.sin(theta) * radius) + xCurrent;
            world.spawnParticle(Particle.COMPOSTER, x, yCurrent, z, 20, 0.2, 0.2 ,0.2);
        }
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

}