package uk.co.drnaylor.antipotionfield;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardInit;

public class AntiPotionField extends JavaPlugin {
    
    Properties _props;
    String _version;
    public static AntiPotionField plugin;
    public static RegionConfig regions;
    CommandsExecWG wgCE;
    PlayerEventRegionsHandler _WGeventHandler;
    
    
    @Override
    public void onEnable() {
        
        plugin = this;
        try {
            _props = getPropertiesFromClasspath("resource.properties");
            _version = _props.getProperty("app.version");
        } catch (IOException ex) {
            getLogger().warning(ex.getMessage());
            _version = "unknown";
        }
        
        if (!WorldGuardInit.checkWG(this)) {
            getLogger().info("[AntiPotionField] WorldGuard has NOT been found. Disabling.");
            this.getPluginLoader().disablePlugin(this);
        }
        
        Util.InitArrays();
        
        regions = new RegionConfig("regions.yml");
                    //Register /flyregion command with the WorldGuard extension
            wgCE = new CommandsExecWG();
            getCommand("antipotionregion").setExecutor(wgCE);

            //Register PlayerMoveEvent and PlayerToggleFlyingEvent
            _WGeventHandler = new PlayerEventRegionsHandler();
            getServer().getPluginManager().registerEvents(_WGeventHandler, this);   
            
    }
    
    @Override
    public void onDisable() {
        regions.getRegionConfig().saveConfig();
    }
    
    
    public Properties getPropertiesFromClasspath(String propFileName) throws IOException {
        // loading xmlProfileGen.properties from the classpath
        Properties props = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
        //.getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream == null) {
            throw new FileNotFoundException("property file '" + propFileName
                    + "' not found in the classpath");
        }

        props.load(inputStream);

        return props;
    }
    
}
