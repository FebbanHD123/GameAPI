/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.map;

import com.google.common.collect.Maps;
import de.febanhd.gameapi.api.GameAPI;
import de.febanhd.gameapi.api.team.Teams;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MapLoader {

    private final File mapFile;
    private boolean teams;
    private FileConfiguration cfg;
    private MapManager mapManager;

    public MapLoader(File mapFile, MapManager mapManager) {
        this.mapFile = mapFile;
        this.teams = GameAPI.getInstance().getGame().isUsingTeams();
        this.cfg = YamlConfiguration.loadConfiguration(mapFile);
        this.mapManager = mapManager;
    }

    public boolean canLoadMap(boolean showErrors) {
        if(!this.cfg.contains("displayName")) {
            if(showErrors)
                GameAPIBootstrap.sendConsoleMessage("Can't load map because it didn't has a displayName! Delete it an recreate this map!");
            return false;
        }
        if(!this.cfg.contains("spectatorLocation") || this.mapManager.getSpectatorLocation(this.mapFile) == null) {
            if(showErrors)
                GameAPIBootstrap.sendConsoleMessage("Can't load spectatorLocation!");
            return false;
        }
        if(this.teams) {
            ArrayList<Teams> usedTeams = GameAPI.getInstance().getGame().getTeams();
            for (Teams team : usedTeams) {
                if (this.mapManager.getTeamLocation(this.mapFile, team) == null) {
                    if(showErrors)
                        GameAPIBootstrap.sendConsoleMessage("Can't load map because it didn't has a spawn for every team in this game.");
                    return false;
                } ;
            }
        }else {
            int maxPlayers = GameAPI.getInstance().getGame().getMaxPlayers();
            if(this.mapManager.loadPlayerLocations(mapFile).size() < maxPlayers) {
                if(showErrors)
                    GameAPIBootstrap.sendConsoleMessage("Can't load map because it didn't has a spawn for every player (" + maxPlayers + ") in this game.");
                return false;
            }
        }
        return true;
    }

    public MapObject load() {
        GameAPIBootstrap.sendConsoleMessage("§aLoading map: " + this.mapFile.getName());
        if(this.canLoadMap(true)) {
            HashMap<Teams, Location> teamsLocations = null;
            ArrayList<Location> playerSpawnLocations = null;
            if(this.teams) {
                teamsLocations = Maps.newHashMap();
                ArrayList<Teams> usedTeams = GameAPI.getInstance().getGame().getTeams();
                for(Teams team : usedTeams) {
                    teamsLocations.put(team, this.mapManager.getTeamLocation(this.mapFile, team));
                }
            }else {
                playerSpawnLocations = this.mapManager.loadPlayerLocations(this.mapFile);
            }
            String displayName = this.cfg.getString("displayName");
            Location spectatorLocation = this.mapManager.getSpectatorLocation(this.mapFile);
            if(this.teams) {
                return new MapObject(displayName, spectatorLocation, teamsLocations);
            }else {
                return new MapObject(displayName, spectatorLocation, playerSpawnLocations);
            }
        }
        return null;
    }
}
