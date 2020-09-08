/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api;

import de.febanhd.gameapi.api.game.GameInitializer;

import de.febanhd.gameapi.api.game.RunningGame;
import de.febanhd.gameapi.api.gamestate.AbstractLobbyState;
import de.febanhd.gameapi.api.gamestate.GameState;
import de.febanhd.gameapi.backend.GameAPIBootstrap;
import de.febanhd.gameapi.backend.exceptions.AlreadyRegisteredGameException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
public class GameAPI {

    private static GameAPI instance;

    private GameState currentGameState;

    private RunningGame runningGame;

    private GameAPI() {}

    public void setCurrentGameState(GameState gameState) {
        GameAPIBootstrap.sendConsoleMessage("Set GameState to '" + gameState.getClass().getSimpleName() + "'");

        if(this.currentGameState != null) {
            this.currentGameState.onDisable();
        }
        this.currentGameState = gameState;
        this.currentGameState.onEnable();
    }

    @SneakyThrows
    public void registerGame(GameInitializer gameInitializer) {
        if(this.runningGame == null) {
            this.runningGame = new RunningGame(this);
            this.runningGame.initGame(gameInitializer);
        }else {
            throw new AlreadyRegisteredGameException();
        }
    }

    public static GameAPI getInstance() {
        if(instance == null)
            instance = new GameAPI();
        return instance;
    }
}
