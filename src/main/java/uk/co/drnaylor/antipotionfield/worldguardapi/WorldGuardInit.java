package uk.co.drnaylor.antipotionfield.worldguardapi;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;

//Class to check for WorldGuard availability. It can not be used as an object, as all members are static. It is thus abstract.
public abstract class WorldGuardInit {

    private static boolean _isAvailable;
    private static WorldGuardPlugin wgPlugin;

    //Instantiate the static variable
    static {
        _isAvailable = false;
    }

    /**
     * This function checks to see if WorldGuard is enabled on the system without
     * going through the checks again.
     *
     * @return <b>true</b> if WorldGuard is available, <b>false</b> otherwise.
     */
    public static boolean wgAvailable() {
        return _isAvailable;
    }

    /**
     * This is a getter method, so we don't override the variable. Just in case.
     * We can only modify this variable within the class (so, only if we are re-checking for WG)
     *
     * @return WorldGuardPlugin object containing the plugin, null otherwise
     */
    public static WorldGuardPlugin getWGPlugin() {
        return wgPlugin;
    }

    //Is WG loaded?

    /**
     * This method checks to see if WorldGuard is loaded.
     *
     * @param host Reference to the currently running plugin (the class that extends JavaPlugin)
     * @return <b>true</b> if Worldguard is available, <b>false</b> otherwise.
     */
    public static boolean checkWG(Plugin host) {
        //Is there a plugin with the name WorldGuard?
        Plugin plugin = host.getServer().getPluginManager().getPlugin("WorldGuard");

        //If we have found a plugin by the name of "WorldGuard", check if it is actually
        //WorldGuard. If not, or if we didn't find it, then it's not loaded in.
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            _isAvailable = false;
            return false; //Nope, it's not loaded.
        }

        //Tell this class where the plugin is.
        wgPlugin = (WorldGuardPlugin) plugin;

        //Set the _isAvailable flag to false
        _isAvailable = true;
        return true;
    }

}
