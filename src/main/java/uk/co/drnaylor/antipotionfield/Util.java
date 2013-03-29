package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.co.drnaylor.antipotionfield.enums.EffectMeans;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardAPIException;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardInterface;

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
	
	/**
	 * Gets the list of regions at a player's location and returns them in a StringList.
	 * @param player The player whose location we want to check for regions.
	 * @return regionlist The list of regions at the player's location.
	 */
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

	/**
	 * Gets the list of denied effects at the player's location.
	 * @param player The player whose location we want to check.
	 * @return deniedEffects A StringList of denied effects at that location.
	 */
	public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player) {
		List<String> regionlist = getRegionsAtPlayerLoc(player);
		if (regionlist == null || regionlist.isEmpty()) {
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
	
	/**
	 * Gets the list of denied effects at the player's location.
	 * @param player The player whose location we want to check.
	 * @param apl An EffectMeans specifying whether we want to check thrown effects, potion effects, or applied effects.
	 * @return deniedEffects A StringList of the specified denied effects at that location.
	 */
	public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player, EffectMeans apl) {
		
		/*
		I can't make new classes through GitHub, so I'm going to write down a gist of the enum I'll make later.
		
		public enum EffectMeans {
			EFFECT, // For if we are checking applied effects
			POTION, // For if we want to check potions that are consumed
			SPLASH  // For if we want to check potions that are thrown
		}
		*/
		
		List<String> regionlist = getRegionsAtPlayerLoc(player);
		if (regionlist == null || regionlist.isEmpty()) {
			return null; //If there's no regions, then there's no effects!
		}
		
		ArrayList <PotionEffectType> deniedEffects = new ArrayList <PotionEffectType> ();
		
		for (String s : regionlist) {
			for (String e : AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + s + ".deny-" + apl.toString() + "s")) {
				if (!deniedEffects.contains(PotionEffectType.getByName(e))) { //To prevent duplicate entries
					deniedEffects.add(PotionEffectType.getByName(e));
				}
			}
		}
		return deniedEffects;
	}
	
	/**
	 * Checks to see if the passed Player can use a potion with effect type.
	 * @param player The player whose location we will check for denied effects.
	 * @param type The PotionEffectType we are specifically checking for.
	 * @return true if the player is allowed to use that potion, otherwise false.
	 */
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
		if (deniedEffects == null || deniedEffects.isEmpty()) { // If there's a plugin error, no denied effects, or no denied regions...
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

        /**
         * Utility method that removes prohibited effects from a player, dependant on their location.
         * @param player Player to remove effects from.
         */
	public static void removeDisallowedEffects(Player player) {
		if (!player.getActivePotionEffects().isEmpty()) {
			ArrayList <PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);
			boolean notify = false;                        
			for (PotionEffect ce : player.getActivePotionEffects()) {
				if (denEff.contains(ce.getType())) {
					if (!player.hasPermission("worldguard.region.bypass." + player.getWorld().getName()) || !player.hasPermission("antipotionfield.bypass") || !player.isOp() || player.hasPermission("antipotionfield.allowed-potions." + ce.getType().getName())) {
						continue; //Don't remove the effect if they're allowed to have it!
					}
					notify = true;
					player.sendMessage(ChatColor.RED + "Your effect " + ce.getType().getName() + " is disallowed in this area.");
					player.removePotionEffect(ce.getType());
				}
			}
      			if (notify) { // Keep this out of the loop!
				player.sendMessage(ChatColor.RED + "Your disallowed potion effects have been removed.");
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
