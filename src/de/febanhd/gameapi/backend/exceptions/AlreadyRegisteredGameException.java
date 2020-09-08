/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.backend.exceptions;

public class AlreadyRegisteredGameException extends Exception {

    public AlreadyRegisteredGameException() {
        super("Already registered Game");
    }
}
