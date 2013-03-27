package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

	public static List<String> getRegionsAtPlayerLoc(Player player) {
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

	public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player) {
		List<String> regionlist = getRegionsAtPlayerLoc(player);
		if (regionlist == null) {
			return null; //If there's no regions, then there's no effects!
		}
		
		ArrayList <PotionEffectType> deniedEffects = new ArrayList <PotionEffectType> ();
		
		for (String s : regionlist) {
			//For each region, get its denied effect StringList out of the configuration file
			//and add any new denied effects to the deniedEffects ArrayList.
			for (String e : AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + s + ".deny-effects")) {
				if (!deniedEffects.contains(PotionEffectType.getByName(e))) { //To prevent duplicate entries
					deniedEffects.add(PotionEffectType.getByName(e));
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
		} else if (player.hasPermission("antipotionfield.allowed-potions." + type.getName())) {
			return true;
		} else if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains("__global__")) { //If we didn't bypass, and it is not allowed in the region...
			return false;
		}
		
		ArrayList <PotionEffectType> deniedEffects = getDeniedEffectsAtPlayerLoc(player);
		if (deniedEffects == null) { // If there's a plugin error, no denied effects, or no denied regions...
			return true;         // Return true, as there's no reason for us to attempt to stop the event.
		} else if (deniedEffects.contains(type)) { // If the potion/effect they're trying to use is disallowed...
			return false;
		}
		
		//We can use the potion here.
		return true;
		
	}
	
	/**
	 * Gets the player's current effect list, and checks to see if any of them are
	 * disallowed at the player's current location.
	 * @param player The player in question.
	 * @return true if the player has disallowed effects, otherwise false.
	 */
	public static boolean hasDisallowedEffects(Player player) {
		if (player == null) {
			return false; // The calling method should check for this on their own!
		}
		if (player.getActivePotionEffects().isEmpty()) {
			return false; // They have no effects to iterate through in the first place!
		}
		//Get the denied effects of the regions and check to see if any of the player's current effects are disallowed in it.
		ArrayList <PotionEffect> curEffects = (ArrayList <PotionEffect>) player.getActivePotionEffects();
		ArrayList <PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);
		
		for (PotionEffect ce : curEffects) {
			if (denEff.contains(ce.getType())) {
				return true; // The player has an effect that's denied at their current location.
			}
		}
		// If we iterated through all of the player's effects, they must be legal.
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

	public static void removeDisallowedEffects(Player player) {
		if (!player.getActivePotionEffects().isEmpty()) {
			ArrayList <PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);
			boolean notify = false;
			
			for (PotionEffect ce : player.getActivePotionEffects()) {
				if (denEff.contains(ce.getType())) {
					if (!player.hasPermission("worldguard.region.bypass." + player.getWorld().getName()) || !player.hasPermission("antipotionfield.bypass") || !player.isOp() || player.hasPermission("antipotionfield.allowed-potions." + ce.getType())) {
						//Don't remove the effect if they're allowed to have it!
					} else {
						notify = true;
						player.sendMessage(ChatColor.RED + "Your effect " + ce.getType().getName() + " is disallowed in this area.");
						player.removePotionEffect(ce.getType());
					}
				}
			
				if (notify) {
					player.sendMessage(ChatColor.RED + "Your disallowed potion effects have been removed.");
				}
			}
		}
	}
	
	@Deprecated
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
