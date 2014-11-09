package shiftscope.controllers;

import java.net.URI;

/**
 *
 * @author carlos
 */
public class Main {
    public static ShiftScopeWebSocket s;
    public static void main(String[] args) {
        Handlers.buildLibraryTreeFromFile();
        s = new ShiftScopeWebSocket(URI.create("ws://127.0.0.1:8001"));
        s.connect();
        ShiftScopePlayer.initPlayer();
        ViewHandler.init();
    }
}
