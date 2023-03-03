package com.BraydenGolden.chess.ChessAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.github.bhlangonijr.chesslib.Board;

@Entity
public class ChessGame {
    
    private @Id @GeneratedValue long id;
    private final Board board;

    ChessGame(Board board) {
        this.board = board;
    }

}