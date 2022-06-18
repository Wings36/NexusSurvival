package nexusSurvival;

import nexusSurvival.MovePlay.*;
import nexusSurvival.homeTeleporter.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().set("worldtp", true);

        //dabd clock
        TeleporterClock tpClock = new TeleporterClock(this);
        this.getCommand("getclock").setExecutor(new SummonClock(this, tpClock));
        this.getCommand("spawncircle").setExecutor(new SpawnCircle(tpClock));
        this.getCommand("resettp").setExecutor(new ResetTp(tpClock, this));
        this.getCommand("checktp").setExecutor(new CheckTp(tpClock));
        this.getCommand("clockreplace").setExecutor(new ClockTPReplace(this, tpClock));
        this.getCommand("tplookup").setExecutor(new TpLookup(tpClock, this));
        this.getCommand("admintest").setExecutor(new admintest(this));
        this.getCommand("spawnsphere").setExecutor(new SpawnSphere(tpClock));

        getServer().getPluginManager().registerEvents(tpClock, this);

        //Event move spawn
        this.getCommand("StartSpreadEvent").setExecutor(new SpreadStart(this));
        getServer().getPluginManager().registerEvents(new LoginEvent(this), this);
        this.getCommand("banish").setExecutor(new tpStory(this));
        getServer().getPluginManager().registerEvents(new netherban(this), this);
        this.getCommand("toggleWorldTP").setExecutor(new toggletpban(this));
        //
    }

    public void onDisable() {

    }
}
