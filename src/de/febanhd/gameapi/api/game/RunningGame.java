/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.game;

import com.google.common.collect.Lists;
import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.team.Teams;
import de.febanhd.gameapi.api.team.TeamManager;
import lombok.Getter;
import org.apache.commons.lang.NullArgumentException;

import java.util.ArrayList;

@Getter
public class RunningGame {

    private ArrayList<Teams> teams;
    private TeamManager teamManager;
    private boolean usingTeams;

    private final GameAPI gameAPI;
    private GameInitializer gameInitializer;

    private String PREFIX;

    private int minPlayers, maxPlayers;

    public RunningGame(GameAPI gameAPI) {
        this.gameAPI = gameAPI;
        this.teamManager = new TeamManager(this);
    }

    public void initGame(GameInitializer initializer) {
        if(initializer.getLobbyState() == null) {
            throw new NullPointerException("You must set the LobbyState");
        }

        this.gameInitializer = initializer;
        this.PREFIX = initializer.getPrefix();
        this.maxPlayers = initializer.getMaxPlayers();
        this.minPlayers = initializer.getMinPlayers();
        this.usingTeams = initializer.useTeams();
        if(this.usingTeams) {
            teams = Lists.newArrayList(initializer.initializeTeams());
            this.teamManager.registerTeams();
        }

        this.gameAPI.setCurrentGameState(initializer.getLobbyState());
    }
}
