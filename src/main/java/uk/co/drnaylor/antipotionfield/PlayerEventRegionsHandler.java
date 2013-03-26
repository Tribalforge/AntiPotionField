package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

public class PlayerEventRegionsHandler implements Listener {
	
	/**
	 * Checks the player's current effects to see if they are allowed at their location.
	 * 
	 * @param event The PlayerMoveEvent involving the player.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		//See the Util class
		if (!Util.canUsePotions(player)) { // Will be changed to hasDeniedEffects(player) later.
			Util.removePositiveEffects(player);
			
		}
	}
	
	/**
	 * Checks to see if the player is attempting to drink a potion, and checks the potion to see if it is allowed.
	 * 
	 * @param event The PlayerInteractEvent involving the player.
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType() == Material.POTION) {
			if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
				
				// Check the type of potion in the player's hand
				Potion potion = Potion.fromItemStack(event.getPlayer().getItemInHand());
				ArrayList <PotionEffect> effects = (ArrayList <PotionEffect>) potion.getEffects();
				
				boolean cancelEvent = false;
				for (PotionEffect e : effects) {
					if (!(Util.canUsePotion(event.getPlayer(), e.getType()))) {
						
					}
				}
				
				if (cancelEvent) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "You cannot use that potion here!");
					Util.removePositiveEffects(event.getPlayer());
				}
			}
		}
	}
}
