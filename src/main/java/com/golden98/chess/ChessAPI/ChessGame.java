package com.golden98.chess.ChessAPI;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.move.MoveList;
import com.github.bhlangonijr.chesslib.move.Move;

@Entity
public class ChessGame {
    
    private @Id @GeneratedValue long id;
    private String san;
    // do not access this directly, use getBoard()
    transient Board board;

    ChessGame() {
        this.san = "";
    }

    public Board getBoard() {
        if (board != null) {
            return board;
        }
        MoveList moveList = new MoveList();
        moveList.loadFromSan(this.san);
        board = new Board();
        for (Move move : moveList) {
            board.doMove(move);
        }
        return board;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.getBoard().toString();
    }    
    
    public String getSan() {
        return san;
    }

    public void setSan(String san) {
        this.san = san;
    }

    public void updateSan() {
        Board tempBoard = this.getBoard();
        MoveList moveList = new MoveList();
        for (MoveBackup backup: tempBoard.getBackup()) {
            moveList.add(backup.getMove());
        }
        this.san = moveList.toSan();
    }
}