package com.golden98.chess.ChessAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import com.github.bhlangonijr.chesslib.Board;

@Entity
public class ChessGame extends Board {
    
    private @Id @GeneratedValue long id;

    ChessGame() {
    }


}