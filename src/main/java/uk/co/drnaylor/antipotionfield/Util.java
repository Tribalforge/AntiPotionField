package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.co.drnaylor.worldguardapi.WorldGuardAPIException;
import uk.co.drnaylor.worldguardapi.WorldGuardInterface;

public abstract class Util {

	public static Collection<PotionEffectType> positive;

	public static void InitArrays() {
		positive = new ArrayList<PotionEffectType>();		
		positive.add(PotionEffectType.DAMAGE_RESISTANCE);
		positive.add(PotionEffectType.FAST_DIGGING);
		positive.add(PotionEffectType.FIRE_RESISTANCE);
		positive.add(PotionEffectType.HEAL);
		positive.add(PotionEffectType.HUNGER);
		positive.add(PotionEffectType.INCREASE_DAMAGE);
		positive.add(PotionEffectType.INVISIBILITY);
		positive.add(PotionEffectType.JUMP);
		positive.add(PotionEffectType.NIGHT_VISION);
		positive.add(PotionEffectType.REGENERATION);
		positive.add(PotionEffectType.SPEED);
		// positive.add(PotionEffectType.WITHER);
		positive.add(PotionEffectType.WATER_BREATHING);
	}

	public static StringList getRegionsAtPlayerLoc(Player player) {
		WorldGuardInterface wgi;
		ArrayList<String> regionlist;
		try { //Hook into WG
			wgi = new WorldGuardInterface();
			regionlist = wgi.GetRegionNamesAtPoint(player); // Get regions
		} catch (WorldGuardAPIException ex) {
			return null; //If we can't hook in, then we should return.
		}
		return regionlist;
	}

	public static ArrayList<String> getDeniedEffectsAtPlayerLoc(Player player) {
		StringList regionlist = getRegionsAtPlayerLoc(player);
		if (regionlist == null) {
			return null; //If there's no regions, then there's no effects!
		}
		
		ArrayList <String> deniedEffects = new ArrayList <String> (); //Will optimize later
		
		for (String s : regionlist) {
			//For each region, get its denied effect StringList out of the configuration file
			//and add any new denied effects to the deniedEffects ArrayList.
			for (String e : AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-regions." + s + ".deny-effects")) {
				if (!deniedEffects.contains(e)) { //To prevent duplicate entries
					deniedEffects.add(e);
				}	
			}
		}
		return deniedEffects;
	}

	public static boolean canUsePotion(Player player, PotionEffectType type) {
		//If you have the relavent bypass nodes, then you can still use your positive potions.
		if (player.hasPermission("worldguard.region.bypass." + player.getWorld().getName())) {
			return true;
		} else if (player.hasPermission("antipotionfield.bypass")) {
			return true;
		} else if (player.isOp()) {
			return true;
		} else if (player.hasPermission("antipotionfield.canuse." + type.toString())) {
			return true;
		} else if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains("__global__")) { //If we didn't bypass, and it is not allowed in the region...
			return false;
		}

		StringList regionlist = getRegionsAtPlayerLoc(player);
		
		//If there are no regions, then we can return true.
		if (regionlist != null) {
			
			//This section needs to be rewritten, as I've changed my mind about the layout of the configuration file.
			
			//Check for region
			for (String a : regionlist) {
				//Is the selected region a no-potion zone?
				//if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains(a)) {
				if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-regions.deny-" + type.toString()).contains(a)) {
					//Yes. We cannot use the potion here here - so we may as well break and return false now.
					return false;
				}
			}
		}
		//We can use the potion here.
		return true;
		
		
		/* Potion usability will be defined in the config based on a "no-potion-regions.<region>.deny-effects" StringList.
		 * If the name of the current potion effect is found in that StringList, and the player is in
		 * the region specified, the potion will be denied.
		 * This will replace the below method.
		 */
		
	}

	public static boolean hasDisallowedEffets(Player player) {
		//Get the region and check to see if any of the player's current effects are disallowed in it.
		return false;
	}

	@Deprecated
	public static boolean canUsePotions(Player player) {
		//If you have the relavent bypass nodes, then you can still use your positive potions.
		if (player.hasPermission("worldguard.region.bypass." + player.getWorld().getName())) {
			return true;
		} else if (player.hasPermission("antipotionfield.bypass")) {
			return true;
		} else if (player.isOp()) {
			return true;
			//	} else if (player.hasPermission("antipotionfield.canuse.<potionname>")) {
			//		return true;
			//	} else if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("region-potion-deny." + <potion-type> + ...
			//		return false;
		} else if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains("__global__")) { //If we didn't bypass. and it is not allowed in the region...
			return false;
		}

		WorldGuardInterface wgi;
		ArrayList<String> regionlist;
		//Hook into WG
		try
		{
			wgi = new WorldGuardInterface();
			//Get regions
			regionlist = wgi.GetRegionNamesAtPoint(player);
		}
		catch (WorldGuardAPIException ex)
		{
			//If we can't hook in, then we should return true
			return true;
		}

		//If there are no regions, then we can return true.
		if (regionlist != null) {
			//Check for region
			for (String a : regionlist) {
				//Is the selected region a no-fly zone?
				if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains(a)) {
					//Yes. We cannot fly here - so we may as well break and return false now.
					return false;
				}
			}

		}
		//We can fly here.
		return true;
	}


	public static void removePositiveEffects(Player player) {
		if (!player.getActivePotionEffects().isEmpty()) {

			boolean notify = false;
			for (PotionEffect b : player.getActivePotionEffects()) {
				if (positive.contains(b.getType())) {
					player.removePotionEffect(b.getType());
					notify = true;
				}
			}

			if (notify) {
				player.sendMessage(ChatColor.RED + "You are in a Anti Positive Potion area. All positive effects have been removed.");
			}
		}
	}

}
