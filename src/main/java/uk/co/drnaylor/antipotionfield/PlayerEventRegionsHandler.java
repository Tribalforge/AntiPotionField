package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

public class PlayerEventRegionsHandler implements Listener {

    /**
     * Checks the player's current effects to see if they are allowed at their
     * location.
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
     * Checks to see if the player is attempting to drink a potion, and checks
     * the potion to see if it is allowed.
     *
     * @param event The PlayerItemConsumeEvent involving the player.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) {
            return;
        }
        // Check the type of potion in the player's hand
        Potion potion = Potion.fromItemStack(event.getItem());
        ArrayList<PotionEffect> effects = (ArrayList<PotionEffect>) potion.getEffects();

        boolean cancelEvent = false;
        for (PotionEffect e : effects) {
            if (!(Util.canUsePotion(event.getPlayer(), e.getType()))) {
                cancelEvent = true;
            }
        }

        if (cancelEvent) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot use that potion here!");
            Util.removePositiveEffects(event.getPlayer());
        }
    }
}
