/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.commands;

import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameAPICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(GameAPIBootstrap.PREFIX + "§7GameAPI by FebanHD, Version " + GameAPIBootstrap.getInstance().getDescription().getVersion());
        sender.sendMessage(GameAPIBootstrap.PREFIX + "§7Copyright (c) 2020");
        return false;
    }
}
