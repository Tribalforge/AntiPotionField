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
@Deprecated
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

			/**
			 * New command syntax:
			 * /antipotionregion <region> <allow|deny> <potion|positive|negative|all> [type]
			 * 
			 * <region> is the region name; <allow|deny> specifies whether to allow or deny the potion;
			 * <potion> is the effect type (or predefined list of types); [type] is optional and defines whether the potion is splash, drink, or effect.
			 * If [type] isn't provided, the plugin will assume that it must be allowed or denied for all types.
			 */

			if (args.length == 0) { // No arguments provided!
				sender.sendMessage(ChatColor.GREEN + "Usage: " + ChatColor.YELLOW + "/antipotionregion <region> <allow|deny>");	
			} else if (args.length == 1) { // They have written something like "/antipotionregion <region>".
				try {
					WorldGuardInterface wgi = new WorldGuardInterface();
					List<World> worlds;
					if (sender instanceof Player) {
						Player player = ((Player) sender).getPlayer();
						World world = player.getWorld();
						worlds = new ArrayList<World>();
						worlds.add(world);
					} else {
						worlds = AntiPotionField.plugin.getServer().getWorlds();
					}

					for (World world : worlds) {
						ProtectedRegion rg = wgi.getRegion(world, args[0]);
						if (rg != null) { // If the region exists...
							if (AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection("no-potion-effect-regions." + args[0])) {
								// If a section for this region exists in the configuration file...
								sender.sendMessage(ChatColor.YELLOW + "This region denies the following effects and types:");

								String ef1 = ChatColor.YELLOW + "Denied effects: " + ChatColor.GRAY;
								String ef2 = ChatColor.YELLOW + "Denied potions: " + ChatColor.GRAY;
								String ef3 = ChatColor.YELLOW + "Denied splashes: " + ChatColor.GRAY;

								for (List<String> s1 : AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection("no-potion-effect-regions." + args[0] + ".deny-effects")) {
									ef1.concat(s1 + ", ");
									// I realize this will end the list with a comma, but I can't be bothered to fix that right now...
								}
								for (List<String> s2 : AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection("no-potion-effect-regions." + args[0] + ".deny-potions")) {
									ef2.concat(s2 + ", ");
								}
								for (List<String> s3 : AntiPotionField.regions.getRegionConfig().getConfig().isConfigurationSection("no-potion-effect-regions." + args[0] + ".deny-splashes")) {
									ef3.concat(s3 + ", ");
								}

								sender.sendMessage(ef1);
								sender.sendMessage(ef2);
								sender.sendMessage(ef3);

							} else {
								sender.sendMessage(ChatColor.YELLOW + "This region has no denied potions associated with it.");
							}
						} else {
							sender.sendMessage(ChatColor.YELLOW + "The region " + args[0] + " cannot be found.");
						}
					}


				} catch (WorldGuardAPIException e) {
					sender.sendMessage(ChatColor.YELLOW + "WorldGuard is not currently enabled");
				}
				
				
			} else if (args.length == 2) {
				try {
					WorldGuardInterface wgi = new WorldGuardInterface();
					World world;
					boolean deny;
					if ("allow".equals(args[1].toLowerCase())) {
						deny = false;
					} else if ("deny".equals(args[1].toLowerCase())) {
						deny = true;
					} else {
						sender.sendMessage(ChatColor.RED + "Second parameter must be \"allow\" or \"deny\"");
						return true;
					}

					if (sender instanceof Player) {
						Player player = ((Player) sender).getPlayer();
						world = player.getWorld();
					} else {
						sender.sendMessage(ChatColor.RED + "Hey Console! You need to specify the world too! (antipotionregion <world> <region> <allow|deny>");
						return true;
						//worlds = staticReferences.plugin.getServer().getWorlds();
					}

					List<String> regionList;
					//Let's load in any changes first
					loadRegionConfig();
					regionList = AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + world.getName());

					//World check
					if ("__global__".equalsIgnoreCase(args[0])) {
						//If we currently have the region in a list, we deny it
						if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + world.getName()).contains("__global__")) {
							//We want to allow it
							if (!deny) {
								regionList.remove("__global__");
								sender.sendMessage(ChatColor.GREEN + "Potions are now enabled in WG region in world \"" + world.getName() + "\"");
							} //Already denied
							else {
								//Means that we want to allow, but we already allow!
								sender.sendMessage(ChatColor.GREEN + "Potions are already disabled in WG region in world \"" + world.getName() + "\"");
							}
						} else {
							//We want to deny it.
							if (deny) {
								regionList.add("__global__");
								sender.sendMessage(ChatColor.GREEN + "Potions are now disabled in WG region in world \"" + world.getName() + "\"");
							} //Already allowed
							else {
								//Means that we want to allow, but we already allow it!
								sender.sendMessage(ChatColor.GREEN + "Potions are already enabled in WG region in world \"" + world.getName() + "\"");
							}
						}
						//Save all changes
						AntiPotionField.regions.getRegionConfig().getConfig().set("denypositiveregions." + world.getName(), regionList);
						saveRegionConfig();
						return true;
					}


					//Region check
					ProtectedRegion rg = wgi.GetRegion(world, args[0]);
					if (rg != null) {
						//If we currently have the region in a list, we deny it
						if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + world.getName()).contains(rg.getId())) {
							//We want to allow it
							if (!deny) {
								regionList.remove(rg.getId());
								sender.sendMessage(ChatColor.GREEN + "Potions are now enabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							} //Already denied
							else {
								//Means that we want to allow, but we already allow!
								sender.sendMessage(ChatColor.GREEN + "Potions are already disabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							}
						} else {
							//We want to deny it.
							if (deny) {
								regionList.add(rg.getId());
								sender.sendMessage(ChatColor.GREEN + "Potions are now disabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							} //Already allowed
							else {
								//Means that we want to allow, but we already allow it!
								sender.sendMessage(ChatColor.GREEN + "Potions are already enabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							}
						}
						//Save all changes
						AntiPotionField.regions.getRegionConfig().getConfig().set("denypositiveregions." + world.getName(), regionList);
						saveRegionConfig();
						return true;
					}
					sender.sendMessage(ChatColor.YELLOW + "The region " + args[0] + " cannot be found.");
					return true;
				} catch (WorldGuardAPIException ex) {
					sender.sendMessage(ChatColor.YELLOW + "WorldGuard is not currently enabled");
				}
			} else if (args.length == 3) { // /flyregion <world> <rg> <allow|deny>
				try {
					WorldGuardInterface wgi = new WorldGuardInterface();
					World world;
					boolean deny;
					if ("allow".equals(args[2].toLowerCase())) {
						deny = false;
					} else if ("deny".equals(args[2].toLowerCase())) {
						deny = true;
					} else {
						sender.sendMessage(ChatColor.RED + "Third parameter must be \"allow\" or \"deny\"");
						return true;
					}

					world = AntiPotionField.plugin.getServer().getWorld(args[0]);

					if (world == null) {
						sender.sendMessage(ChatColor.RED + "World \"" + args[0] + "\" does not exist!");
						return true;
					}

					List<String> regionList;
					//Let's load in any changes first
					loadRegionConfig();
					regionList = AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions.\"" + world.getName() + "\"");

					//World check
					if ("__global__".equalsIgnoreCase(args[1])) {
						//If we currently have the region in a list, we deny it
						if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions." + world.getName()).contains("__global__")) {
							//We want to allow it
							if (!deny) {
								regionList.remove("__global__");
								sender.sendMessage(ChatColor.GREEN + "Potions are now enabled in WG region in world \"" + world.getName() + "\"");
							} //Already denied
							else {
								//Means that we want to allow, but we already allow!
								sender.sendMessage(ChatColor.GREEN + "Potions are already disabled in WG region in world \"" + world.getName() + "\"");
							}
						} else {
							//We want to deny it.
							if (deny) {
								regionList.add("__global__");
								sender.sendMessage(ChatColor.GREEN + "Potions are now disabled in WG region in world \"" + world.getName() + "\"");
							} //Already allowed
							else {
								//Means that we want to allow, but we already allow it!
								sender.sendMessage(ChatColor.GREEN + "Potions are already enabled in WG region in world \"" + world.getName() + "\"");
							}
						}
						//Save all changes
						AntiPotionField.regions.getRegionConfig().getConfig().set("denypositiveregions." + world.getName(), regionList);
						saveRegionConfig();
						return true;
					}


					ProtectedRegion rg = wgi.GetRegion(world, args[1]);
					if (rg != null) {
						//If we currently have the region in a list, we deny it
						if (AntiPotionField.regions.getRegionConfig().getConfig().getStringList("denypositiveregions.\"" + world.getName() + "\"").contains(rg.getId())) {
							//We want to allow it
							if (!deny) {
								regionList.remove(rg.getId());
								sender.sendMessage(ChatColor.GREEN + "Potions are now enabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							} //Already denied
							else {
								//Means that we want to allow, but we already allow!
								sender.sendMessage(ChatColor.GREEN + "Potions are already disabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							}
						} else {
							//We want to deny it.
							if (deny) {
								regionList.add(rg.getId());
								sender.sendMessage(ChatColor.GREEN + "Potions are now disabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							} //Already allowed
							else {
								//Means that we want to allow, but we already allow it!
								sender.sendMessage(ChatColor.GREEN + "Potions are already enabled in WG region " + rg.getId() + " in world \"" + world.getName() + "\"");
							}
						}
						//Save all changes
						AntiPotionField.regions.getRegionConfig().getConfig().set("denypositiveregions.\"" + world.getName() + "\"", regionList);
						saveRegionConfig();
						return true;
					}
					sender.sendMessage(ChatColor.YELLOW + "The region " + args[0] + " cannot be found.");
					return true;
				}
				catch (WorldGuardAPIException e)
				{
					if (e.except == WorldGuardExceptions.NotEnabled)
					{
						sender.sendMessage(ChatColor.YELLOW + "WorldGuard is not currently enabled");
					}
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
