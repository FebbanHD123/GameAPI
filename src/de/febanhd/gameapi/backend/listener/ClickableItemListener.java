/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.listener;

import de.febanhd.gameapi.api.items.ClickableItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickableItemListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() != null) {
            for(ClickableItemStack stack : ClickableItemStack.stacks) {
                if(stack.equals(event.getItem())) {
                    stack.getListener().onClick(event);
                }
            }
        }
    }
}
