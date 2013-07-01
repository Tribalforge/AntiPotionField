package uk.co.drnaylor.antipotionfield;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import uk.co.drnaylor.antipotionfield.enums.EffectMeans;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardAPIException;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Util {

    public static List<PotionEffectType> positive;
    public static List<PotionEffectType> negative;
    public static List<PotionEffectType> all;


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

        negative = new ArrayList<PotionEffectType>();
        negative.add(PotionEffectType.BLINDNESS);
        negative.add(PotionEffectType.CONFUSION);
        negative.add(PotionEffectType.HARM);
        negative.add(PotionEffectType.HUNGER);
        negative.add(PotionEffectType.POISON);
        negative.add(PotionEffectType.SLOW);
        negative.add(PotionEffectType.SLOW_DIGGING);
        negative.add(PotionEffectType.WEAKNESS);
        negative.add(PotionEffectType.WITHER);

        all = new ArrayList<PotionEffectType>();
        all.addAll(positive);
        all.addAll(negative);
    }

    /**
     * Gets the configuration-friendly effect names for a passed String array.
     * It will avoid adding duplicates to the list through the use of a Set.
     *
     * @param args The passed String array to translate into friendly effect names.
     * @return A StringList with the requested names. The List will be empty if there were no matches.
     */
    public static List<String> getFriendlyEffectNames(String[] args) {
        Set<String> effects = new HashSet<String>();
        for (String arg : args) {
            if (arg.equalsIgnoreCase("blindness") || arg.equalsIgnoreCase("blind")) {
                effects.add(PotionEffectType.BLINDNESS.getName());
            } else if (arg.equalsIgnoreCase("confusion") || arg.equalsIgnoreCase("confuse")) {
                effects.add(PotionEffectType.CONFUSION.getName());
            } else if (arg.equalsIgnoreCase("resistance") || arg.equalsIgnoreCase("damageresist") || arg.equalsIgnoreCase("damageresistance")) {
                effects.add(PotionEffectType.DAMAGE_RESISTANCE.getName());
            } else if (arg.equalsIgnoreCase("haste") || arg.equalsIgnoreCase("fastdigging")) {
                effects.add(PotionEffectType.FAST_DIGGING.getName());
            } else if (arg.equalsIgnoreCase("fireresistance") || arg.equalsIgnoreCase("fireres")) {
                effects.add(PotionEffectType.FIRE_RESISTANCE.getName());
            } else if (arg.equalsIgnoreCase("harming") || arg.equalsIgnoreCase("harm")) {
                effects.add(PotionEffectType.HARM.getName());
            } else if (arg.equalsIgnoreCase("healing") || arg.equalsIgnoreCase("heal")) {
                effects.add(PotionEffectType.HEAL.getName());
            } else if (arg.equalsIgnoreCase("starving") || arg.equalsIgnoreCase("starvation") || arg.equalsIgnoreCase("hunger")) {
                effects.add(PotionEffectType.HUNGER.getName());
            } else if (arg.equalsIgnoreCase("strength") || arg.equalsIgnoreCase("increasedamage")) {
                effects.add(PotionEffectType.INCREASE_DAMAGE.getName());
            } else if (arg.equalsIgnoreCase("invisibility") || arg.equalsIgnoreCase("invis")) {
                effects.add(PotionEffectType.INVISIBILITY.getName());
            } else if (arg.equalsIgnoreCase("jump") || arg.equalsIgnoreCase("height")) {
                effects.add(PotionEffectType.JUMP.getName());
            } else if (arg.equalsIgnoreCase("nightvision")) {
                effects.add(PotionEffectType.NIGHT_VISION.getName());
            } else if (arg.equalsIgnoreCase("poison") || arg.equalsIgnoreCase("poisoning")) {
                effects.add(PotionEffectType.POISON.getName());
            } else if (arg.equalsIgnoreCase("regeneration") || arg.equalsIgnoreCase("regen")) {
                effects.add(PotionEffectType.REGENERATION.getName());
            } else if (arg.equalsIgnoreCase("slowness") || arg.equalsIgnoreCase("slow")) {
                effects.add(PotionEffectType.SLOW.getName());
            } else if (arg.equalsIgnoreCase("slowdigging") || arg.equalsIgnoreCase("slow_digging")) {
                effects.add(PotionEffectType.SLOW_DIGGING.getName());
            } else if (arg.equalsIgnoreCase("swiftness") || arg.equalsIgnoreCase("speed")) {
                effects.add(PotionEffectType.SPEED.getName());
            } else if (arg.equalsIgnoreCase("waterbreathing") || arg.equalsIgnoreCase("respiration")) {
                effects.add(PotionEffectType.WATER_BREATHING.getName());
            } else if (arg.equalsIgnoreCase("weakness") || arg.equalsIgnoreCase("weak")) {
                effects.add(PotionEffectType.WEAKNESS.getName());
            } else if (arg.equalsIgnoreCase("wither") || arg.equalsIgnoreCase("withering")) {
                effects.add(PotionEffectType.WITHER.getName());
            } else if (arg.equalsIgnoreCase("positive")) { // If the String wants all positive effects...
                for (PotionEffectType p : positive) {
                    effects.add(p.getName());
                }
            } else if (arg.equalsIgnoreCase("negative")) { // If the String wants all negative effects...
                for (PotionEffectType n : negative) {
                    effects.add(n.getName());
                }
            } else if (arg.equalsIgnoreCase("all")) { // If the String wants all effects...
                for (PotionEffectType a : all) {
                    effects.add(a.getName());
                }
            }
        }

        // Convert the Set into a List and return it.
        return new ArrayList<String>(effects);
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
            for (String e : AntiPotionField.getRegionConfig().getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + s + ".deny-effects")) {
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
     * @param apl    An EffectMeans specifying whether we want to check thrown
     *               effects, potion effects, or applied effects.
     * @return deniedEffects A StringList of the specified denied effects at
     *         that location.
     */
    public static ArrayList<PotionEffectType> getDeniedEffectsAtPlayerLoc(Player player, EffectMeans apl) {
        player.sendMessage(ChatColor.GREEN + "Getting denied effects at your location");
        List<String> regionlist = getRegionsAtPlayerLoc(player);
        if (regionlist == null || regionlist.isEmpty()) {
            return null; //If there's no regions, then there's no effects!
        }

        ArrayList<PotionEffectType> deniedEffects = new ArrayList<PotionEffectType>();

        String apls = apl.toString().toLowerCase();
        if (apls.equalsIgnoreCase("splash")) {
            apls = apls.concat("es");
        } else {
            apls = apls.concat("s");
        }

        for (String s : regionlist) {
            for (String e : AntiPotionField.getRegionConfig().getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + s + ".deny-" + apls)) {
                if (!deniedEffects.contains(PotionEffectType.getByName(e))) { //To prevent duplicate entries
                    deniedEffects.add(PotionEffectType.getByName(e));
                    player.sendMessage(ChatColor.GREEN + "Effect " + e + " is denied");
                }
            }
        }

        return deniedEffects;
    }

    /**
     * Checks to see if the passed Player can use a potion with effect type.
     *
     * @param player The player whose location we will check for denied effects.
     * @param type   The PotionEffectType we are specifically checking for.
     * @return true if the player is allowed to use that potion, otherwise
     *         false.
     */
    public static boolean canUsePotion(Player player, PotionEffectType type) {
        player.sendMessage(ChatColor.GREEN + "Checking your potion types...");
        //If you have the relavent bypass nodes, then you can still use your positive potions.
        if (player.hasPermission("worldguard.region.bypass." + player.getWorld().getName())) {
        		player.sendMessage(ChatColor.GREEN + "You have region bypass permissions.");
            return true;
        } else if (player.hasPermission("antipotionfield.bypass")) {
        		player.sendMessage(ChatColor.GREEN + "You have APR bypass permissions.");
            return true;
        } else if (player.isOp()) {
        	player.sendMessage(ChatColor.GREEN + "You're an op! You can bypass.");
            return true;
        } else if (player.hasPermission("antipotionfield.allowed-potions." + type.getName())) {
        	player.sendMessage(ChatColor.GREEN + "You're allowed to have that effect.");
            return true;
        } else if (AntiPotionField.getRegionConfig().getRegionConfig().getConfig().getStringList("denypositiveregions." + player.getWorld().getName()).contains("__global__")) { //If we didn't bypass, and it is not allowed in the region...
        	player.sendMessage(ChatColor.GREEN + "That last line is returning false.");
            return false;
        }

        ArrayList<PotionEffectType> deniedEffects = getDeniedEffectsAtPlayerLoc(player);
        if (deniedEffects == null || deniedEffects.isEmpty()) { // If there's a plugin error, no denied effects, or no denied regions...
        	player.sendMessage(ChatColor.GREEN + "There's no denied effects at your location.");
            return true;         // Return true, as there's no reason for us to attempt to stop the event.
        } else if (deniedEffects.contains(type)) { // If the potion/effect they're trying to use is disallowed...
        	player.sendMessage(ChatColor.RED + "That potion effect is disallowed!");
            return false;
        }

        //We can use the potion here.
    	player.sendMessage(ChatColor.GREEN + "You can use that effect.");
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
            player.sendMessage(ChatColor.GREEN + "You have no denied effects.");
            return false; // They have no effects to iterate through in the first place!
        }
        //Get the denied effects of the regions and check to see if any of the player's current effects are disallowed in it.
        ArrayList<PotionEffect> curEffects = (ArrayList<PotionEffect>) player.getActivePotionEffects();
        ArrayList<PotionEffectType> denEff = getDeniedEffectsAtPlayerLoc(player);

        for (PotionEffect ce : curEffects) {
            if (denEff.contains(ce.getType())) {
                player.sendMessage(ChatColor.RED + "You have a denied effect!");
                return true; // The player has an effect that's denied at their current location.
            }
        }
        // If we iterated through all of the player's effects, they must be legal.
        player.sendMessage(ChatColor.GREEN + "All of your effects are legal.");
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
            player.sendMessage(ChatColor.DARK_GREEN + "You have potion effects!");
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
