/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.util;

import org.bukkit.Location;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class LocationUtil {

    public static String locationToString(Location location) {
        try {
            ByteArrayOutputStream str = new ByteArrayOutputStream();
            BukkitObjectOutputStream stream = new BukkitObjectOutputStream(str);
            stream.writeObject(location);
            stream.close();
            str.close();
            return Base64.getEncoder().encodeToString(str.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Location locationFromString(String data) {
        try {
            ByteArrayInputStream str = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream in = new BukkitObjectInputStream(str);
            Location location = (Location) in.readObject();
            str.close();
            in.close();
            return location;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
