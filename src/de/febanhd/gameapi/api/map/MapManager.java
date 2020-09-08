/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.map;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import de.febanhd.gameapi.api.team.Teams;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import de.febanhd.gameapi.backend.util.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Seekable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MapManager {

    @Getter
    private File fileStorage = new File(GameAPIBootstrap.getInstance().getDataFolder().getPath() + "/maps");

    public ArrayList<MapLoader> getValidMapToLoad() {
        ArrayList<MapLoader> loaders = Lists.newArrayList();
        File[] files = this.fileStorage.listFiles();
        if(files != null) {
            for(File file : files) {
                MapLoader loader = new MapLoader(file, this);
                if(loader.canLoadMap(false)) {
                    loaders.add(loader);
                }
            }
        }
        return loaders;
    }

    public void createMap(String name) {
        File file = this.getMapFileByName(name);
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("locations", new ArrayList<Location>());
        this.update(file, "playerLocations", json);
        this.update(file, "displayName", name);
    }

    public void deleteMap(String name) {
        File file = this.getMapFileByName(name);
        if (file.exists())
            file.delete();
    }

    public void setSpectatorLocation(String name, Location location) {
        this.update(this.getMapFileByName(name), "spectatorLocation", LocationUtil.locationToString(location));
    }

    public Location getSpectatorLocation(File file) {
        return LocationUtil.locationFromString((String) this.query(file, "pectatorLocation"));
    }

    public void setTeamLocation(String name, Teams team, Location location) {
        this.update(this.getMapFileByName(name), "teamLocation." + team.toString(), LocationUtil.locationToString(location));
    }

    public Location getTeamLocation(File file, Teams team) {
        if(!this.contains(file, "teamLocation." + team.toString())) return null;
        return LocationUtil.locationFromString((String) this.query(file, "teamLocation." + team.toString()));
    }

    public void addPlayerSpawn(String name, Location location) {
        ArrayList<Location> locations = this.loadPlayerLocations(this.getMapFileByName(name));
        assert locations != null : "Locations konnten nicht geladen werden!";
        locations.add(location);
    }

    public ArrayList<Location> loadPlayerLocations(File file) {
        Object jsonString = this.query(file, "playerLocations");
        if(jsonString == null) return null;
        JSONObject json = new JSONObject(jsonString);
        JSONArray locationStringArray = json.getJSONArray("locations");
        ArrayList<Location> locations = Lists.newArrayList();
        locationStringArray.forEach(o -> {
            locations.add(LocationUtil.locationFromString(String.valueOf(o)));
        });
        return locations;
    }

    private void update(File mapFile, String path, Object value) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(mapFile);
        cfg.set(path, value);

        try {
            cfg.save(mapFile);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object query(File mapFile, String path) {
        return YamlConfiguration.loadConfiguration(mapFile).get(path);
    }

    private boolean contains(File mapFile, String path) {
        return YamlConfiguration.loadConfiguration(mapFile).contains(path);
    }

    public File getMapFileByName(String name) {
        return new File(this.fileStorage, name.toLowerCase() + ".yml");
    }

}
