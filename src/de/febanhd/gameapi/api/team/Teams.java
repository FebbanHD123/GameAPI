/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.team;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Color;

@Getter
public enum Teams {

    AQUA(ChatColor.AQUA, "Hellblau", Color.AQUA),
    BLACK(ChatColor.BLACK, "Schwarz", Color.BLACK),
    BLUE(ChatColor.BLUE, "Blau", Color.BLUE),
    GOLD(ChatColor.GOLD, "Gold", Color.ORANGE),
    GREEN(ChatColor.GREEN, "Grün", Color.GREEN),
    DARK_GREEN(ChatColor.DARK_GREEN, "Olive", Color.OLIVE),
    PURPLE(ChatColor.LIGHT_PURPLE, "Pink", Color.FUCHSIA),
    DARK_PURPLE(ChatColor.DARK_PURPLE, "Violett", Color.PURPLE),
    YELLOW(ChatColor.YELLOW, "Gelb", Color.YELLOW),
    RED(ChatColor.RED, "Rot", Color.RED),
    WHITE(ChatColor.WHITE, "Weiß", Color.WHITE),
    SPECTATOR(ChatColor.DARK_GRAY, "§8【§c✘§8】", Color.GRAY),
    NO_TEAM(ChatColor.GRAY, "", Color.GRAY);

    private final ChatColor color;
    private final String name;
    private final Color armorColor;


    Teams(ChatColor color, String name, Color armorColor) {
        this.color = color;
        this.name = name;
        this.armorColor = armorColor;
    }
}
