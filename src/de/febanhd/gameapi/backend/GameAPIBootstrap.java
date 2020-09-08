/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend;

import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.backend.commands.GameAPICommand;
import de.febanhd.gameapi.backend.listener.APIListener;
import de.febanhd.gameapi.backend.listener.ClickableItemListener;
import de.febanhd.gameapi.backend.prefix.PrefixHandler;
import lombok.Getter;
import org.bukkit.Bukkit;

public class GameAPIBootstrap extends org.bukkit.plugin.java.JavaPlugin {

    private static GameAPIBootstrap instance;

    public static final String PREFIX = "§8[§cGameAPI§8] §r";
    @Getter
    private PrefixHandler prefixHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.prefixHandler = new PrefixHandler();
        Bukkit.getPluginManager().registerEvents(new APIListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClickableItemListener(), this);
        this.getCommand("gameapi").setExecutor(new GameAPICommand());
        GameAPIBootstrap.sendConsoleMessage("§aEnabled GameAPI V. " + this.getDescription().getVersion() + " by FebanHD");
    }

    @Override
    public void onDisable() {


        GameAPIBootstrap.sendConsoleMessage("§cDisabled GameAPI V. " + this.getDescription().getVersion() + " by FebanHD");
    }

    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(GameAPIBootstrap.PREFIX + message);
    }

    public static GameAPIBootstrap getInstance() {
        return instance;
    }
}
