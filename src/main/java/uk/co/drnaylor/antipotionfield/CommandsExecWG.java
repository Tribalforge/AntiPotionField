package uk.co.drnaylor.antipotionfield;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardAPIException;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardAPIException.WorldGuardExceptions;
import uk.co.drnaylor.antipotionfield.worldguardapi.WorldGuardInterface;

/**
 * This class is currently being renovated to account for new changes.
 */
public class CommandsExecWG implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (cmd.getName().equalsIgnoreCase("antipotionregion")) {
			//No parameters, 
			boolean perm;
			if (sender instanceof Player) {
				//Do we have config perms?
				if (sender.hasPermission("antipotionfield.regions") || sender.isOp()) {
					perm = true;
				} else {
					perm = false;
				}
			} 
			else {
				perm = true;
			} //console!

			if (!perm) {
				sender.sendMessage(ChatColor.RED + "Sorry, but you don't have permission to do that!");
				return true;
			}

			/*
			 * New command syntax:
			 * /antipotionregion <region> <allow|deny> <potion|positive|negative|all> [type]
			 * 
			 * <region> is the region name; <allow|deny> specifies whether to allow or deny the potion;
			 * <potion> is the effect type (or predefined list of types); [type] is optional and defines whether the potion is splash, drink, or effect.
			 * If [type] isn't provided, the plugin will assume that it must be allowed or denied for all types.
			 */

			if (args.length == 0) { // No arguments provided!
				sender.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.YELLOW + "/antipotionregion <region> <allow|deny>");	
			
			} else if (args.length == 1 || args.length == 2) { // They have written something like "/antipotionregion <region>" or "/antipotionregion <world> <region>".
				int argsOffset;
				try {
					WorldGuardInterface wgi = new WorldGuardInterface();
					List<World> worlds;
					if (sender instanceof Player) {
						if (args.length == 2) { // Too many arguments!
							sender.sendMessage(ChatColor.RED + "Too many arguments!");
							return true;
						}
						// The args has to be 1 here for a player otherwise.
						Player player = ((Player) sender).getPlayer();
						World world = player.getWorld();
						worlds = new ArrayList<World>();
						worlds.add(world);
						argsOffset = 1;
					} else { // This is the console!
						if (args.length == 2) { // They provided a world - /antipotionregion <world> <region>
							if (sender.getServer().getWorld(args[0]) != null) {
								worlds.add(sender.getServer().getWorld(args[0]));
							} else {
								sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't a world! Listing all matching regions from all worlds.");
								worlds = AntiPotionField.plugin.getServer().getWorlds();
							}
							argsOffset = 0;
						} else { // They only gave a region name - /antipotionregion <region> - like a player!
							sender.sendMessage(ChatColor.YELLOW + "Listing all matching regions from all worlds.");
							worlds = AntiPotionField.plugin.getServer().getWorlds();
							argsOffset = 1;
						}
					}

					for (World world : worlds) {
						ProtectedRegion rg = wgi.GetRegion(world, args[1 - argsOffset]);
						if (rg != null) { // If the region exists...
							if (AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset])) {
								// If a section for this region exists in the configuration file...
								
								List<String> ls1 = AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-effects");
								List<String> ls2 = AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-potions");
								List<String> ls3 = AntiPotionField.regions.getRegionConfig().getConfig().getStringList("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-splashes");
								boolean ex1, ex2, ex3;
								String ef1 = ChatColor.GOLD + "Denied effects: " + ChatColor.YELLOW;
								String ef2 = ChatColor.GOLD + "Denied potions: " + ChatColor.YELLOW;
								String ef3 = ChatColor.GOLD + "Denied splashes: " + ChatColor.YELLOW;
								
								if (ls1 != null && !(ls1.isEmpty())) {
									ex1 = true; // The first list exists
									for (String s1 : ls1) {
										ef1.concat(s1 + ", ");
									}
									ef1 = ef1.substring(0, ef1.lastIndexOf(",") - 1); // Thanks, dualspiral!
								} else {
									ex1 = false; // The first list doesn't exist, or is empty.
								}
								
								if (ls2 != null && !(ls2.isEmpty())) {
									ex2 = true;
									for (String s2 : ls2) {
										ef2.concat(s2 + ", ");
									}
									ef2 = ef2.substring(0, ef2.lastIndexOf(",") - 1);
								} else {
									ex2 = false;
								}
								
								if (ls3 != null && !(ls3.isEmpty())) {
									ex3 = true;
									for (String s3 : ls3) {
										ef3.concat(s3 + ", ");
									}
									ef3 = ef3.substring(0, ef3.lastIndexOf(",") - 1);
								} else {
									ex3 = false;
								}
								
								if (ex1 == false && ex2 == false && ex3 == false) { // If all three lists are empty or don't exist...
									sender.sendMessage(ChatColor.YELLOW + "This region has no denied potions associated with it.");
									return true;
								} else { // There's something we can print out!
									sender.sendMessage(ChatColor.YELLOW + "This region denies the following effects and types:");
									if (ex1) { sender.sendMessage(ef1); }
									if (ex2) { sender.sendMessage(ef2); }
									if (ex3) { sender.sendMessage(ef3); }
								}
							} else {
								sender.sendMessage(ChatColor.YELLOW + "This region has no denied potions associated with it.");
								return true;
							}
						} else { // The region doesn't exist!
							sender.sendMessage(ChatColor.YELLOW + "The region " + args[0] + " cannot be found.");
							return true;
						}
					}


				} catch (WorldGuardAPIException e) {
					sender.sendMessage(ChatColor.DARK_RED + "WorldGuard is not currently enabled!");
					return true;
				}
			
			} else if (args.length >= 3) { // If they provide the region, allow|deny, an effect, and a possible type
				
				try {
					WorldGuardInterface wgi = new WorldGuardInterface();
					World world;
					ProtectedRegion rg;
					int argsOffset = 0;
					
					if (!(sender instanceof Player)) {
						// The console must supply a world, so they must have four arguments (optionally five).
						// /antipotionregion worldName regionName allow|deny potionEffect type
						if (args.length > 3) { // If the console provided at least four arguments...
							argsOffset = 0;
							
							// args[0] should be the world.
							world = sender.getServer().getWorld(args[0]);
							if (world == null) {
								sender.sendMessage(ChatColor.RED + "World " + args[0] + " doesn't exist!");
								return true;
							}
							
						} else {
							sender.sendMessage(ChatColor.YELLOW + "Hey, Console! We need the world name as well!");
							sender.sendMessage(ChatColor.GREEN + "/antipotionregion <world> <region> <allow|deny> <potion> [type]");
							return true;
						}
					} else { // The command sender is a Player.
						argsOffset = 1;
						Player player = (Player)sender;
						world = player.getWorld();
						if (world == null) { // Just in case.
							sender.sendMessage(ChatColor.RED + "You're apparently not in a world!");
							return true;
						}
					}
					
					rg = wgi.GetRegion(world, args[1 - argsOffset]); // The region name is args[0] for the player, or args[1] for the console.
					
					if (rg != null) { // If the region exists...
						
						String configPath = "no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset];
						if (!(AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection(configPath))) {
							
							/*
							If this region has no configuration section in the config, set the following paths:
							
							"no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-effects"
							"no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-potions"
							"no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-splashes"
							
							Then save the configuration.
							*/
							
							loadRegionConfig();
							AntiPotionField.regions.getRegionConfig().getConfig().set("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-effects", new ArrayList<String> ());
							AntiPotionField.regions.getRegionConfig().getConfig().set("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-potions", new ArrayList<String> ());
							AntiPotionField.regions.getRegionConfig().getConfig().set("no-potion-effect-regions." + world.getName() + "." + args[1 - argsOffset] + ".deny-splashes", new ArrayList<String> ());
							saveRegionConfig();
						}
						
						// Get whether we're adding or removing from the list!
						boolean denying;
						if (args[2 - argsOffset].equalsIgnoreCase("allow") || args[2 - argsOffset].equalsIgnoreCase("a")) {
							denying = false;
						} else if (args[2 - argsOffset].equalsIgnoreCase("deny") || args[2 - argsOffset].equalsIgnoreCase("d")) {
							denying = true;
						} else { // Catch other letters
                            sender.sendMessage(ChatColor.RED + "Incorrect parameter (expecting allow or deny)!");
                            return true;
                        }
                        
						
						// Get the potion types through the Util class using args[3 - argsOffset]
						//List<String> effects = Util.getEffectString(args[3 - argsOffset]);
						String[] pArgs = args[3 - argsOffset].split(",");
						//List<String> effects = Util.getFriendlyEffectNames(pArgs);
						List<String> effects = new ArrayList<String> ();
						for (int k = 0; k < pArgs.length; k++) {
							List<String> newEff = Util.getFriendlyEffectNames(new String[] {pArgs[k]});
							if (newEff.isEmpty()) {
								sender.sendMessage(ChatColor.RED + "\"" + pArgs[k] + "\" isn't a recognizable potion effect!");
							} else {
								for (String s : newEff) {
									effects.add(s);
								}
							}
						}
						if (effects.isEmpty()) {
						//	sender.sendMessage(ChatColor.RED + "\"" + args[3 - argsOffset] + "\" isn't a potion effect or predefined list!");
							sender.sendMessage(ChatColor.RED + "No applicable potion effects were found.");
							return true;
						}
						
						// If there's a fourth (or fifth) argument, use it to get the effect type. (args[4 - argsOffset])
						// Otherwise, just apply it to every compatible list!
						ArrayList<String> paths = new ArrayList<String> (3);
						if (args.length >= (4 - argsOffset)) {
							if (args[4 - argsOffset].equalsIgnoreCase("EFFECT")) {
								paths.add(configPath + ".deny-effects");
							} else if (args[4 - argsOffset].equalsIgnoreCase("POTION")) {
								paths.add(configPath + ".deny-potions");
							} else if (args[4 - argsOffset].equalsIgnoreCase("SPLASH")) {
								paths.add(configPath + ".deny-splashes");
							} else { // They gave us something like "derp" for a type!
								sender.sendMessage(ChatColor.RED + "\"" + args[4 - argsOffset] + "\" isn't a recognizable effect type!");
								sender.sendMessage(ChatColor.RED + "Acceptable types: \"effect\", \"potion\", \"splash\"");
								return true;
							}
						} else {
							paths.add(configPath + ".deny-effects");
							paths.add(configPath + ".deny-potions");
							paths.add(configPath + ".deny-splashes");
						}
						
						// We should have everything we need at this point, so let's start doing things!
						loadRegionConfig();
						
						for (String curPath : paths) {
							List<String> regionEffectList = AntiPotionField.regions.getRegionConfig().getConfig().getStringList(curPath);
							
							String currentType = "";
							if (curPath.contains("deny-effects")) {
								currentType = "Effect";
							} else if (curPath.contains("deny-potions")) {
								currentType = "Potion";
							} else if (curPath.contains("deny-splashes")) {
								currentType = "Splash";
							}
							
							if (denying) { // If we are adding to the list...
								for (String e : effects) {
									if (!(regionEffectList.contains(e))) { // If the list doesn't already have this effect in it...
										regionEffectList.add(e); // Add the new effect on to the end!
										sender.sendMessage(ChatColor.YELLOW + "Denied " + currentType.toLowerCase() + "  \"" + e + "\" in region \"" + args[1 - argsOffset] + "\".");
									} else { // This effect is already denied.
										sender.sendMessage(ChatColor.GRAY + currentType + " \"" + e + "\" is already denied in region \"" + args[1 - argsOffset] + "\".");
									}
								}
								
								AntiPotionField.regions.getRegionConfig().getConfig().set(curPath, regionEffectList); // Set the new list to the configuration file.
								
							} else { // If we are removing from the list...
								for (String e : effects) {
									if (regionEffectList.contains(e)) { // If the list has this effect, we need to remove it now!
										regionEffectList.remove(e); // Remove the effect.
										sender.sendMessage(ChatColor.YELLOW + "Allowed " currentType.toLowerCase() + " \"" + e + "\" in region \"" + args[1 - argsOffset] + "\".");
									} else { // This effect is already allowed - that is, it's not in the list.
										sender.sendMessage(ChatColor.GRAY + currentType + " \"" + e + "\" is already allowed in region \"" + args[1 - argsOffset] + "\".");
									}
								}
								
								AntiPotionField.regions.getRegionConfig().getConfig().set(curPath, regionEffectList); // Set the new list to the configuration file.
							}
							
						}
						
						saveRegionConfig(); // Save the new configuration.
						sender.sendMessage(ChatColor.GREEN + "All changes saved successfully.");
						return true;
						
					} else {
						sender.sendMessage(ChatColor.RED + "The region " + args[1 - argsOffset] + " does not exist!");
						return true;
					}
					
				} catch (WorldGuardAPIException error) {
					sender.sendMessage(ChatColor.DARK_RED + "WorldGuard is not currently enabled!");
					return true;
				}
			}
			
		}

		return false;
	}



	public void saveRegionConfig() {
		//regions.getRegionConfig().getConfig().set("regions", regionList);
		AntiPotionField.regions.getRegionConfig().saveConfig();
	}

	public void loadRegionConfig() {
		AntiPotionField.regions.getRegionConfig().reloadConfig();
	}
}
