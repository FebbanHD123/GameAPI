/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.prefix;

import com.google.common.collect.Maps;
import de.febanhd.gameapi.api.team.Teams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class PrefixHandler {

    private Scoreboard board;
    private HashMap<Teams, Team> teams = Maps.newHashMap();

    public PrefixHandler() {
        this.registerBoard();
    }

    private void registerBoard() {
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        int current = 10;
        for(Teams team : Teams.values()) {
            String teamName = current + team.toString().toLowerCase();
            board.registerNewTeam(teamName);
            board.getTeam(teamName).setPrefix(String.valueOf(team.getColor()));
            this.teams.put(team, board.getTeam(teamName));
        }
    }

    public void setTeamPrefix(Player player, Teams team) {
        player.setScoreboard(this.board);
        player.setDisplayName(team.getColor() + ChatColor.stripColor(player.getDisplayName()));
        Team scoreboardTeam = this.teams.get(team);
        this.board.getTeam(scoreboardTeam.getName()).addEntry(player.getName());
    }


}
