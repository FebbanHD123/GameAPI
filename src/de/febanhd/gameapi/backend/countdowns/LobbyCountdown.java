/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.countdowns;

import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.countdown.Countdown;
import de.febanhd.gameapi.api.countdown.CountdownListener;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import de.febanhd.gameapi.backend.util.ActionbarUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LobbyCountdown extends Countdown {

    private int idleTaskID;
    private String points = "";
    private int pointAnimation = 0;

    @Getter
    private boolean idle;

    public LobbyCountdown(CountdownListener listener, int time, Integer... triggerNumbers) {
        super(listener, time, triggerNumbers);
    }

    public void startIdle() {
        if(this.running) {
            this.stop();
            this.reset();
        }
        this.running = false;
        this.idle = true;
        this.idleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameAPIBootstrap.getInstance(), () -> {

            String message = "§7Es werden noch §c" + (GameAPI.getInstance().getGame().getMinPlayers() - Bukkit.getOnlinePlayers().size()) + " §7Spiele benötigt " + points;
            for(Player player : Bukkit.getOnlinePlayers())
                ActionbarUtil.sendActionBar(player, message);

            switch (this.pointAnimation) {
                case 0:
                    this.points = "●••";
                    this.pointAnimation++;
                    break;
                case 1:
                    this.points = "•●•";
                    this.pointAnimation++;
                    break;
                case 2:
                    this.points = "••●";
                    this.pointAnimation++;
                    break;
                case 3:
                    this.points = "•●•";
                    this.pointAnimation = 0;
                    break;
            }
        }, 0, 10);
    }

    public void stopIdle() {
        this.idle = false;
        Bukkit.getScheduler().cancelTask(this.idleTaskID);
        this.running = true;
        this.start();
    }
}
