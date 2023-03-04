package com.golden98.chess.ChessAPI;

public class ChessGameNotFoundException extends RuntimeException {
    ChessGameNotFoundException(Long id) {
        super("Game with an id of " + id + " not found");
    }
}