package uk.co.drnaylor.antipotionfield.worldguardapi;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WorldGuardInterface {

    private WorldGuardPlugin _wgPlugin;

    //Instantiate the class and obtain the plugin address from the Init class.
    public WorldGuardInterface() throws WorldGuardAPIException {
        _wgPlugin = WorldGuardInit.getWGPlugin();
        if (_wgPlugin == null) {
            throw new WorldGuardAPIException();
        }
    }

    /**
     * Get the regions at the players' location
     *
     * @param player The player you wish to get the ApplicableRegions from
     * @return A list of strings with the names of the applicable regions. This is null if there are no regions where the player is.
     */
    public ArrayList<String> GetRegionNamesAtPoint(Player player) throws WorldGuardAPIException {
        if (_wgPlugin == null) {
            throw new WorldGuardAPIException();
        }
        //This "wrapping" is required for WorldGuard APIs
        LocalPlayer lplayer = _wgPlugin.wrapPlayer(player);
        //We require a WE vector rather than a location.
        Vector pt = lplayer.getPosition();
        //Get the region manager for the world we are currently in.
        RegionManager rm = _wgPlugin.getRegionManager(player.getWorld());
        //This is the set of regions we are in.
        ApplicableRegionSet set = rm.getApplicableRegions(pt);
        //Just return null if we get nothing.
        if (set.size() == 0) {
            return null;
        }
        //Put the region names in a list.
        ArrayList<String> regions = new ArrayList<String>();
        //For each Region in the set, just add the string with the region name to the return variable.
        for (ProtectedRegion region : set) {
            regions.add(region.getId());
        }
        //Return the list
        return regions;
    }

    /**
     * Gets the specified region in the specified world. Throws WorldGuardAPIException if it fails
     *
     * @param world World to look in
     * @param rg    Region to look for
     * @return The WorldGuardPlugin.ProtectedRegion object if it exists, null otherwise
     */
    public ProtectedRegion GetRegion(World world, String rg) throws WorldGuardAPIException {
        if (_wgPlugin == null) {
            throw new WorldGuardAPIException();
        }
        //Returns the region if it exists, null otherwise.
        return _wgPlugin.getRegionManager(world).getRegion(rg);
    }
}
