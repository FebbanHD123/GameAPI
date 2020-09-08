/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.map;

import com.google.common.collect.Lists;
import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.team.TeamManager;
import de.febanhd.gameapi.api.team.Teams;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@Getter
public class MapObject {

    private final String name;
    private final Location spectatorLocation;
    private HashMap<Teams, Location> teamSpawns;
    private ArrayList<Location> playerSpawns;
    private boolean usingTeams;

    public MapObject(String name, Location spectatorLocation, HashMap<Teams, Location> teamSpawns) {
        this.name = name;
        this.spectatorLocation = spectatorLocation;
        this.teamSpawns = teamSpawns;
        this.usingTeams = true;
    }

    public MapObject(String name, Location spectatorLocation, ArrayList<Location> playerSpawns) {
        this.name = name;
        this.spectatorLocation = spectatorLocation;
        this.playerSpawns = playerSpawns;
        this.usingTeams = false;
    }

    public void teleportPlayers() {
        if(this.usingTeams) {
            TeamManager teamManager = GameAPI.getInstance().getGame().getTeamManager();
            for(Teams team : teamManager.getRegisteredTeams())  {
                Location location = this.teamSpawns.get(team);
                for(Player player : teamManager.getPlayersOfTeam(team)) {
                    player.teleport(location);
                }
            }
        }else {
            Collections.shuffle(this.playerSpawns);
            ArrayList<Player> players = Lists.newArrayList(Bukkit.getOnlinePlayers());
            for(int i = 0; i < players.size(); i++) {
                Location location = this.playerSpawns.get(i);
                Player player = players.get(i);
                player.teleport(location);
            }
        }
    }
}
