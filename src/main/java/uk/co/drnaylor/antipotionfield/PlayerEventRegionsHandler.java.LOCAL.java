package uk.co.drnaylor.antipotionfield;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerEventRegionsHandler implements Listener {

    /**
     * Checks the player's current effects to see if they are allowed at their
     * location.
     *
     * @param event The PlayerMoveEvent involving the player.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        //See the Util class
        Util.removeDisallowedEffects(player);
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
            Util.removeDisallowedEffects(event.getPlayer());
        }
    }
    
    /**
     * Acts upon a potion splash event.
     * If the potion is not thrown by a player, it's allowed to pass.
     * If it was thrown by a player, however, it checks to see if the potion's effect was allowed.
     * @param event The PotionSplashEvent event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
    	if (event.getPotion().getShooter() instanceof Player) { // If a player threw the potion...
    		ArrayList <PotionEffectType> deniedEffects = Util.getDeniedEffectsAtPlayerLoc((Player)event.getPotion().getShooter());
    		ArrayList <PotionEffect> potionEffects = (ArrayList <PotionEffect>) event.getPotion().getEffects();
    		if (deniedEffects == null || potionEffects == null) {
    			return;
    		}
    		
    		//ArrayList <PotionEffect> canceledEffects = new ArrayList <PotionEffect> ();
    		for (PotionEffect pe : potionEffects) {
    			if (deniedEffects.contains(pe.getType())) {
    				//canceledEffects.add(pe);
    				//event.getPotion().getEffects().remove(pe);
    				event.setCancelled(true); // We can try more complex checks later.
    				Util.removeDisallowedEffects((Player)event.getPotion().getShooter());
    			}
    		}
    		
    		//if (canceledEffects.size() <= 0) {
    		//	return;
    		//}
    		/*
    		Collection <LivingEntity> affected = event.getAffectedEntities();
    		for (LivingEntity p : affected) {
    			if (p instanceof Player) {
    				
    			}
    		} */
    		
    	}
    }
}
