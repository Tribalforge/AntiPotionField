package uk.co.drnaylor.antipotionfield;

import org.bukkit.plugin.java.JavaPlugin;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardInit;

public class AntiPotionField extends JavaPlugin {

    private static AntiPotionField plugin;
    private static RegionConfig regions;
    CommandsExecWG wgCE;
    PlayerEventRegionsHandler _WGeventHandler;


    @Override
    public void onEnable() {

        plugin = this;

        if (!WorldGuardInit.checkWG(this)) {
            getLogger().severe("[AntiPotionField] WorldGuard has NOT been found. Disabling.");
            this.getPluginLoader().disablePlugin(this);
        }

        Util.InitArrays();

        regions = new RegionConfig("regions.yml");
        wgCE = new CommandsExecWG();
        getCommand("antipotionregion").setExecutor(wgCE);

        //Register PlayerMoveEvent and PlayerToggleFlyingEvent
        _WGeventHandler = new PlayerEventRegionsHandler();
        getServer().getPluginManager().registerEvents(_WGeventHandler, this);
        getLogger().info("AntiPotionField " + this.getDescription().getVersion() + " by dualspiral and Eevables is now enabled.");
    }

    @Override
    public void onDisable() {
        regions.getRegionConfig().saveConfig();
        getLogger().info("AntiPotionField " + this.getDescription().getVersion() + " disabled.");
    }

    /**
     * Gets the base class of the plugin.
     *
     * @return The plugin.
     */
    public static AntiPotionField getPlugin() {
        return plugin;
    }

    /**
     * Gets the region configuration class
     *
     * @return
     */
    public static RegionConfig getRegionConfig() {
        return regions;
    }
}
