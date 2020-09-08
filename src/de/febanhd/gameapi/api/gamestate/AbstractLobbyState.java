/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.gamestate;

import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.countdown.CountdownListener;
import de.febanhd.gameapi.api.game.RunningGame;
import de.febanhd.gameapi.backend.countdowns.LobbyCountdown;
import de.febanhd.gameapi.backend.util.ActionbarUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract  class AbstractLobbyState extends CountdownListener implements GameState {

    private RunningGame game;
    @Getter
    private LobbyCountdown lobbyCountdown;

    @Override
    public void onEnable() {
        this.game = GameAPI.getInstance().getRunningGame();

        this.lobbyCountdown = new LobbyCountdown(this, 60, 60, 45, 30, 15, 10, 5, 4, 3, 2, 1);

        this.lobbyCountdown.startIdle();
    }

    @Override
    public void onDisable() {
        if(this.game.isUsingTeams()) {
            this.game.getTeamManager().calculateRestPlayers();
        }
    }

    public abstract void setNextGameState();

    @Override
    public void onCountdownTrigger(int time) {
        String message;
        if (time != 0)
            message = "§7Das Spiel startet in §a" + time + " §7Sekunden.";
        else
            message = "§7Das Spiel startet in §a" + time + " §7Sekunde.";

        Bukkit.broadcastMessage(GameAPI.getInstance().getRunningGame().getPREFIX() + message);

        Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_BASS, 2, 1));
    }

    @Override
    public void onCountdownFinish() {
        this.setNextGameState();
    }

    @Override
    public void onEverySecond(int time) {
        String message;
        if (time != 0)
            message = "§7Das Spiel startet in §a" + time + " §7Sekunden";
        else
            message = "§7Das Spiel startet in §a" + time + " §7Sekunde";

        for(Player player : Bukkit.getOnlinePlayers()) {
            ActionbarUtil.sendActionBar(player, message);
        }
    }
}
