/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.game;

import de.febanhd.gameapi.api.gamestate.AbstractLobbyState;
import de.febanhd.gameapi.api.team.Teams;

import java.util.List;

public interface GameInitializer {

    AbstractLobbyState getLobbyState();

    int getMaxPlayers();

    int getMinPlayers();

    List<Teams> initializeTeams();

    int getMaxTeamPlayers();

    boolean useTeams();

    String getPrefix();

    boolean useAutoPlayerPrefix();

    boolean canBreakBlocks();

}
