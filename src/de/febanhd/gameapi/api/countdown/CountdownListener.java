/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.countdown;

public abstract class CountdownListener {

    public abstract void onCountdownTrigger(int time);

    public void onCountdownFinish() {}

    public void onCountdownHalt() {}

     public void onCountdownStop(){}

     public void onCountdownReset(){}

     public void onEverySecond(int time) {}

}
