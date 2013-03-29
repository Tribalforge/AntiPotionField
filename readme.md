#AntiPotionField

AntiPotionField is a simple plugin that hooks into WorldGuard and set regions where no positive effects can be applied to players and prevents players from drinking potions. This is particually useful for PvP arenas where you want players to fight on an even keel.


Permissions
-
   
* ``` antipotionfield.regions ``` - Allows player to define anti-potion regions</li>
* ``` antipotionfield.bypass ``` - Allows player to use potions in no-potion zones and to keep the effects of positive potions when entering these regions.
        
Command Usage
-
        
```/antipotionregion <world> [region] [allow|deny]``` - Requires ```antipotionfield.regions```. Aliases - ```/apf```, ```/apr```, ```/antipotionfield```

Allows or denies the use of positive potions in the region [region], in world &lt;world>. World can be omitted if you are a player, in which case the current world would be used.
       

-
Licenced under the MIT licence. (c) 2013 Dr Daniel R. Naylor (dualspiral) + Eevables Modevet
