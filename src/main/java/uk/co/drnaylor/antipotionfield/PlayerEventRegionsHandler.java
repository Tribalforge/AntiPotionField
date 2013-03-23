package uk.co.drnaylor.antipotionfield;

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

public class PlayerEventRegionsHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		final Player player = event.getPlayer();
		//See the Util class
		if (!Util.canUsePotion(player)) {
			Util.removePositiveEffects(player);

			//player.removePotionEffect(PotionEffectType.SLOW);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getPlayer().getItemInHand().getType() == Material.POTION) {
			if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
				
				// Check the type of potion in the player's hand right here
				
				if (!Util.canUsePotion(event.getPlayer())) { // Add the potion type as an argument for checking

					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "You cannot use potions here!");
					Util.removePositiveEffects(event.getPlayer());
				}
			}
		}
	}
}
