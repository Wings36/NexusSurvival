package nexusSurvival;

import nexusSurvival.MovePlay.SpreadStart;
import nexusSurvival.homeTeleporter.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

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
        //
    }

    public void onDisable() {

    }
}
