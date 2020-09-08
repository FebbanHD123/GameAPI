/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.countdown;

import com.google.common.collect.Lists;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Countdown {

    private final int TIME;
    @Getter
    @Setter
    private int currentTime;
    private int taskID;

    @Getter
    protected boolean running;

    private final CountdownListener listener;

    private ArrayList<Integer> triggerNumbers;

    public Countdown(CountdownListener listener, int time, Integer... triggerNumbers) {
        this.listener = listener;
        if(time <= 0) {
            throw new IllegalArgumentException("Time must be positive");
        }
        this.TIME = time;
        this.currentTime = this.TIME;

        this.triggerNumbers = Lists.newArrayList();
        this.triggerNumbers.addAll(Arrays.asList(triggerNumbers));

        this.running = false;
    }

    public void start() {
        this.running = true;
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(GameAPIBootstrap.getInstance(), () -> {
            if(this.triggerNumbers.contains(this.currentTime)) {
                this.listener.onCountdownTrigger(this.currentTime);
            }
            this.listener.onEverySecond(this.currentTime);

            if(currentTime <= 0) {
                this.finish();
            }

            currentTime--;
        }, 0, 20);
    }

    public void halt() {
        this.listener.onCountdownHalt();
    }

    public void reset() {
        this.currentTime = this.TIME;
        this.listener.onCountdownReset();
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(this.taskID);
        this.listener.onCountdownStop();
    }

    public void finish() {
        this.stop();
        this.reset();
        this.listener.onCountdownFinish();
    }


}
