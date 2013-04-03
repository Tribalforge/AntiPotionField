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
    public static Collection<PotionEffectType> negative;
    public static Collection<PotionEffectType> all;
	
	
	//For the sake of my reference, the names of all PotionEffectTypes:
	//BLINDNESS, CONFUSION, DAMAGE_RESISTANCE, FAST_DIGGING, FIRE_RESISTANCE, HARM, HEAL,
	//HUNGER, INCREASE_DAMAGE, INVISIBILITY, JUMP, NIGHT_VISION, POISON, REGENERATION, SLOW,
	//SLOW_DIGGING, SPEED, WATER_BREATHING, WEAKNESS, WITHER
    public static void InitArrays() {
        positive = new ArrayList<PotionEffectType>();
        positive.add(PotionEffectType.DAMAGE_RESISTANCE);
        positive.add(PotionEffectType.FAST_DIGGING);
        positive.add(PotionEffectType.FIRE_RESISTANCE);
        positive.add(PotionEffectType.HEAL);
        positive.add(PotionEffectType.INCREASE_DAMAGE);
        positive.add(PotionEffectType.INVISIBILITY);
        positive.add(PotionEffectType.JUMP);
        positive.add(PotionEffectType.NIGHT_VISION);
        positive.add(PotionEffectType.REGENERATION);
        positive.add(PotionEffectType.SPEED);
        positive.add(PotionEffectType.WATER_BREATHING);
        
        negative = new ArrayList<PotionEffectType> ();
        negative.add(PotionEffectType.BLINDNESS);
        negative.add(PotionEffectType.CONFUSION);
        negative.add(PotionEffectType.HARM);
        negative.add(PotionEffectType.HUNGER);
        negative.add(PotionEffectType.POISON);
        negative.add(PotionEffectType.SLOW);
        negative.add(PotionEffectType.SLOW_DIGGING);
        negative.add(PotionEffectType.WEAKNESS);
        negative.add(PotionEffectType.WITHER);
        
        all = new ArrayList<PotionEffectType> ();
        for (PotionEffectType p : positive) {
        	all.add(positive.get(p));
        }
        for (PotionEffectType n : negative) {
        	all.add(negative.get(n));
        }
    }
    
	/**
     * Gets the configuration-friendly effect names for a passed String array.
     * It will avoid adding duplicates to the list.
     * @param args The passed String array to translate into friendly effect names.
     * @return A StringList with the requested names. The List will be empty if there were no matches.
     */
    public static List<String> getFriendlyEffectNames(String[] args) {
    	List<String> effects = new List<String> ();
    	for (int c = 0; c < args.length; c++) {
    		if (args[c].equalsIgnoreCase("blindness") || args[c].equalsIgnoreCase("blind")) {
    			if (!(effects.contains(PotionEffectType.BLINDNESS.getName()))) {
    				effects.add(PotionEffectType.BLINDNESS.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("confusion") || args[c].equalsIgnoreCase("confuse")) {
    			if (!(effects.contains(PotionEffectType.CONFUSION.getName()))) {
    				effects.add(PotionEffectType.CONFUSION.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("resistance") || args[c].equalsIgnoreCase("damageresist") || args[c].equalsIgnoreCase("damageresistance")) {
    			if (!(effects.contains(PotionEffectType.DAMAGE_RESISTANCE.getName()))) {
    				effects.add(PotionEffectType.DAMAGE_RESISTANCE.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("haste") || args[c].equalsIgnoreCase("fastdigging")) {
    			if (!(effects.contains(PotionEffectType.FAST_DIGGING.getName()))) {
    				effects.add(PotionEffectType.FAST_DIGGING.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("fireresistance") || args[c].equalsIgnoreCase("fireres")) {
    			if (!(effects.contains(PotionEffectType.FIRE_RESISTANCE.getName()))) {
    				effects.add(PotionEffectType.FIRE_RESISTANCE.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("harming") || args[c].equalsIgnoreCase("harm")) {
    			if (!(effects.contains(PotionEffectType.HARM.getName()))) {
    				effects.add(PotionEffectType.HARM.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("healing") || args[c].equalsIgnoreCase("heal")) {
    			if (!(effects.contains(PotionEffectType.HEAL.getName()))) {
    				effects.add(PotionEffectType.HEAL.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("starving") || args[c].equalsIgnoreCase("starvation") || args[c].equalsIgnoreCase("hunger")) {
    			if (!(effects.contains(PotionEffectType.HUNGER.getName()))) {
    				effects.add(PotionEffectType.HUNGER.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("strength") || args[c].equalsIgnoreCase("increasedamage")) {
    			if (!(effects.contains(PotionEffectType.INCREASE_DAMAGE.getName()))) {
    				effects.add(PotionEffectType.INCREASE_DAMAGE.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("invisibility") || args[c].equalsIgnoreCase("invis")) {
    			if (!(effects.contains(PotionEffectType.INVISIBILITY.getName()))) {
    				effects.add(PotionEffectType.INVISIBILITY.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("jump") || args[c].equalsIgnoreCase("height")) {
    			if (!(effects.contains(PotionEffectType.JUMP.getName()))) {
    				effects.add(PotionEffectType.JUMP.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("nightvision")) {
    			if (!(effects.contains(PotionEffectType.NIGHT_VISION.getName()))) {
    				effects.add(PotionEffectType.NIGHT_VISION.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("poison") || args[c].equalsIgnoreCase("poisoning")) {
    			if (!(effects.contains(PotionEffectType.POISON.getName()))) {
    				effects.add(PotionEffectType.POISON.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("regeneration") || args[c].equalsIgnoreCase("regen")) {
    			if (!(effects.contains(PotionEffectType.REGENERATION.getName()))) {
    				effects.add(PotionEffectType.REGENERATION.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("slowness") || args[c].equalsIgnoreCase("slow")) {
    			if (!(effects.contains(PotionEffectType.SLOW.getName()))) {
    				effects.add(PotionEffectType.SLOW.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("slowdigging") || args[c].equalsIgnoreCase("slow_digging")) {
    			if (!(effects.contains(PotionEffectType.SLOW_DIGGING.getName()))) {
    				effects.add(PotionEffectType.SLOW_DIGGING.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("swiftness") || args[c].equalsIgnoreCase("speed")) {
    			if (!(effects.contains(PotionEffectType.SPEED.getName()))) {
    				effects.add(PotionEffectType.SPEED.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("waterbreathing") || args[c].equalsIgnoreCase("respiration")) {
    			if (!(effects.contains(PotionEffectType.WATER_BREATHING.getName()))) {
    				effects.add(PotionEffectType.WATER_BREATHING.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("weakness") || args[c].equalsIgnoreCase("weak")) {
    			if (!(effects.contains(PotionEffectType.WEAKNESS.getName()))) {
    				effects.add(PotionEffectType.WEAKNESS.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("wither") || args[c].equalsIgnoreCase("withering")) {
    			if (!(effects.contains(PotionEffectType.WITHER.getName()))) {
    				effects.add(PotionEffectType.WITHER.getName());
    			}
    		} else if (args[c].equalsIgnoreCase("positive")) { // If the String wants all positive effects...
    			for (PotionEffectType p : positive) {
    				if (!(effects.contains(p.getName()))) {
    					effects.add(p.getName());
    				}
    			}
    		} else if (args[c].equalsIgnoreCase("negative")) { // If the String wants all negative effects...
    			for (PotionEffectType n : negative) {
    				if (!(effects.contains(n.getName()))) {
    					effects.add(n.getName());
    				}
    			}
    		} else if (args[c].equalsIgnoreCase("all")) { // If the String wants all effects...
    			for (PotionEffectType a : all) {
    				if (!(effects.contains(a.getName()))) {
    					effects.add(a.getName());
    				}
    			}
    		}
    	}
    	return effects;
    }
    

    /**
     * Gets the list of regions at a player's location and returns them in a
     * StringList.
     *
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
     *
     * @param player The player whose location we want to check.
     * @return deniedEffects A StringList of denied effects at that location.
     */
    public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player) {
        List<String> regionlist = getRegionsAtPlayerLoc(player);
        if (regionlist == null || regionlist.isEmpty()) {
            return null; //If there's no regions, then there's no effects!
        }

        ArrayList<PotionEffectType> deniedEffects = new ArrayList<PotionEffectType>();

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
     *
     * @param player The player whose location we want to check.
     * @param apl An EffectMeans specifying whether we want to check thrown
     * effects, potion effects, or applied effects.
     * @return deniedEffects A StringList of the specified denied effects at
     * that location.
     */
    public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player, EffectMeans apl) {

        List<String> regionlist = getRegionsAtPlayerLoc(player);
        if (regionlist == null || regionlist.isEmpty()) {
            return null; //If there's no regions, then there's no effects!
        }

        ArrayList<PotionEffectType> deniedEffects = new ArrayList<PotionEffectType>();

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
     *
     * @param player The player whose location we will check for denied effects.
     * @param type The PotionEffectType we are specifically checking for.
     * @return true if the player is allowed to use that potion, otherwise
     * false.
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

        ArrayList<PotionEffectType> deniedEffects = getDeniedEffectsAtPlayerLoc(player);
        if (deniedEffects == null || deniedEffects.isEmpty()) { // If there's a plugin error, no denied effects, or no denied regions...
            return true;         // Return true, as there's no reason for us to attempt to stop the event.
        } else if (deniedEffects.contains(type)) { // If the potion/effect they're trying to use is disallowed...
            return false;
        }

        //We can use the potion here.
        return true;

    }

    /**
     * Gets the player's current effect list, and checks to see if any of them
     * are disallowed at the player's current location.
     *
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
        ArrayList<PotionEffect> curEffects = (ArrayList<PotionEffect>) player.getActivePotionEffects();
        ArrayList<PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);

        for (PotionEffect ce : curEffects) {
            if (denEff.contains(ce.getType())) {
                return true; // The player has an effect that's denied at their current location.
            }
        }
        // If we iterated through all of the player's effects, they must be legal.
        return false;
    }

    /**
     * Utility method that removes prohibited effects from a player, dependant
     * on their location.
     *
     * @param player Player to remove effects from.
     */
    public static void removeDisallowedEffects(Player player) {
        if (!player.getActivePotionEffects().isEmpty()) {
            ArrayList<PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);
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
