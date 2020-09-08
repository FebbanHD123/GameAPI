/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.listener;

import com.avaje.ebean.annotation.EnumValue;
import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.gamestate.AbstractLobbyState;
import de.febanhd.gameapi.api.team.TeamManager;
import de.febanhd.gameapi.api.team.Teams;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class APIListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        if(GameAPI.getInstance().getCurrentGameState() instanceof AbstractLobbyState) {
            AbstractLobbyState lobbyState = (AbstractLobbyState) GameAPI.getInstance().getCurrentGameState();
            event.setJoinMessage(GameAPI.getInstance().getGame().getPREFIX() + "§a" + player.getDisplayName() + " §7ist dem Spiel §abeigetreten§7.");

            if(Bukkit.getOnlinePlayers().size() >= GameAPI.getInstance().getGame().getMinPlayers() && lobbyState.getLobbyCountdown().isIdle()) {
                lobbyState.getLobbyCountdown().stopIdle();
            }

            Bukkit.getScheduler().runTaskLater(GameAPIBootstrap.getInstance(), () -> {
                //Teleport player to Spawn
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.setFlying(false);
                player.setAllowFlight(false);
                player.setFoodLevel(40);
                player.setHealth(20);
                player.setMaxHealth(20);
            }, 3);
        }
        GameAPI.getInstance().getGame().getTeamManager().setTeam(player, Teams.NO_TEAM);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if(GameAPI.getInstance().getGame() == null) {
            event.disallow(PlayerLoginEvent.Result.ALLOWED, "§cDer Spiel wird noch Registriert!");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TeamManager teamManager = GameAPI.getInstance().getGame().getTeamManager();
        Player player = event.getPlayer();
        event.setQuitMessage(null);

        String quitMessage = GameAPI.getInstance().getGame().getPREFIX() + "§c" + player.getDisplayName() + " §7hat das Spiel §cverlassen§7.";
        if(GameAPI.getInstance().getCurrentGameState() instanceof AbstractLobbyState) {
            event.setQuitMessage(quitMessage);
        }else if(teamManager.getTeamOfPlayer(player) != Teams.SPECTATOR) {
            event.setQuitMessage(quitMessage);
        }
        if(GameAPI.getInstance().getCurrentGameState() instanceof AbstractLobbyState) {
            AbstractLobbyState lobbyState = (AbstractLobbyState) GameAPI.getInstance().getCurrentGameState();
            if(Bukkit.getOnlinePlayers().size() -1 < GameAPI.getInstance().getGame().getMinPlayers() && lobbyState.getLobbyCountdown().isRunning()) {
                lobbyState.getLobbyCountdown().startIdle();
            }
        }

        if(teamManager.isInTeam(player)) {
            teamManager.removePlayerFromTeam(player);
        }
    }
}
