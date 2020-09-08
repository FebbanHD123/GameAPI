/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.febanhd.gameapi.api.game.RunningGame;
import de.febanhd.gameapi.api.items.ClickableItemStack;
import de.febanhd.gameapi.api.items.ClickableItemStackListener;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class TeamManager {

    private HashMap<Teams, ArrayList<UUID>> teamPlayers;
    private final RunningGame game;

    public TeamManager(RunningGame game) {
        this.game = game;
        this.teamPlayers = Maps.newHashMap();
    }

    public void registerTeams() {
        GameAPIBootstrap.sendConsoleMessage("Register teams...");
        List<String> teamNames = Lists.newArrayList();
        game.getTeams().forEach(team -> {
            teamPlayers.put(team, Lists.newArrayList());
            teamNames.add(team.getColor() + team.getName());
        });
        this.teamPlayers.put(Teams.SPECTATOR, Lists.newArrayList());
        this.teamPlayers.put(Teams.NO_TEAM, Lists.newArrayList());
        GameAPIBootstrap.sendConsoleMessage("Registered teams: " + String.join("§8, ", teamNames));
    }

    public void setSpectator(Player player) {
        ItemStack stack = new ClickableItemStack(Material.COMPASS, new ClickableItemStackListener() {
            @Override
            public void onClick(PlayerInteractEvent event) {

            }
        });
        this.setTeam(player, Teams.SPECTATOR);
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFoodLevel(40);
        player.setAllowFlight(true);
        player.setFlying(true);
    }

    public void setTeam(Player player, Teams teams) {
        if(this.getTeamSize(teams) >= this.game.getGameInitializer().getMaxTeamPlayers()) {
            player.sendMessage(this.game.getPREFIX() + "§cDieses Team ist voll.");
            return;
        }

        if(this.isInTeam(player) && this.getTeamOfPlayer(player).equals(teams)) {
            player.sendMessage(this.game.getPREFIX() + "§cDu bist bereits in diesem Team.");
            return;
        }
        if(this.isInTeam(player)) {
            this.removePlayerFromTeam(player);
        }

        this.teamPlayers.get(teams).add(player.getUniqueId());
        if(teams != Teams.SPECTATOR && teams != Teams.NO_TEAM)
            player.sendMessage(this.game.getPREFIX() + "§7Du bist dem Team " + teams.getColor() + teams.getName() + " §7beigetreten.");

        if(this.game.getGameInitializer().useAutoPlayerPrefix()) {
            GameAPIBootstrap.getInstance().getPrefixHandler().setTeamPrefix(player, teams);
        }
    }

    public void removePlayerFromTeam(Player player) {
        if(this.isInTeam(player)) {
            this.teamPlayers.get(this.getTeamOfPlayer(player)).remove(player.getUniqueId());
        }
    }

    public Teams getTeamOfPlayer(Player player) {
        for(Teams team : this.getRegisteredTeams()) {
            if(this.teamPlayers.get(team).contains(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public int getTeamSize(Teams team) {
        return this.teamPlayers.get(team).size();
    }

    public ArrayList<Player> getPlayersOfTeam(Teams team) {
        ArrayList<Player> players = Lists.newArrayList();
        this.teamPlayers.get(team).forEach(uuid -> {
            players.add(Bukkit.getPlayer(uuid));
        });

        return players;
    }

    public boolean isInTeam(Player player) {
        return this.getTeamOfPlayer(player) != null;
    }

    public ArrayList<Teams> getRegisteredTeams() {
        return Lists.newArrayList(this.teamPlayers.keySet());
    }

    public void calculateRestPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!this.isInTeam(player)) {
                Teams team = this.getTeamWithHighestSize(this.getTeamsWithFreeSpace());
                this.setTeam(player, team);
            }
        }
    }

    private Teams getTeamWithHighestSize(ArrayList<Teams> teams) {
        HashMap<Integer, Teams> teamPlayerSize = Maps.newHashMap();
        teams.forEach(team -> {
            teamPlayerSize.put(this.getTeamSize(team), team);
        });
        List<Integer> listToSort = Lists.newArrayList(teamPlayerSize.keySet());
        Collections.sort(listToSort);
        return teamPlayerSize.get(listToSort.get(0));
    }

    private ArrayList<Teams> getTeamsWithFreeSpace() {
        int maxSize = this.game.getGameInitializer().getMaxTeamPlayers();
        ArrayList<Teams> teams = Lists.newArrayList();
        this.getRegisteredTeams().forEach(team -> {
            if(this.getTeamSize(team) < maxSize) {
                teams.add(team);
            }
        });
        return teams;
    }
}
