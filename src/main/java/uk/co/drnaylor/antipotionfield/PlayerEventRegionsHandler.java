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
      if (!Util.canUsePotion(event.getPlayer())) {
          
          if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK) ) {

              if (event.getPlayer().getItemInHand().getType() == Material.POTION) {
                  event.setCancelled(true);
                  event.getPlayer().sendMessage(ChatColor.RED + "You cannot use potions here!");
                  Util.removePositiveEffects(event.getPlayer());
              }
          }
      }
      
      
  }

}
