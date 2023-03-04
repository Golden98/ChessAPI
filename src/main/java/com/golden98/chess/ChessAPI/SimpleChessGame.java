package com.golden98.chess.ChessAPI;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.MoveBackup;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.MoveList;

// a class to only capture what needs to be shown to client
public class SimpleChessGame {
    private long id;
    private String san;
    private Integer moveCounter;
    private Integer halfMoveCounter;
    private Boolean mated;
    private Boolean kingAttacked;
    private Boolean stalemate;
    private Boolean repetition;
    private Boolean insuffencientMaterial;
    private Boolean draw;
    private Side sideToMove;
    private String fen;

    SimpleChessGame(ChessGame chessGame) {
        this.id = chessGame.getId();
        Board board = chessGame.getBoard();
        MoveList moveList = new MoveList();
        for (MoveBackup backup: board.getBackup()) {
            moveList.add(backup.getMove());
        }
        this.san = moveList.toSan();
        this.moveCounter = board.getMoveCounter();
        this.halfMoveCounter = board.getHalfMoveCounter();
        this.mated = board.isMated();
        this.kingAttacked = board.isKingAttacked();
        this.stalemate = board.isStaleMate();
        this.repetition = board.isRepetition();
        this.insuffencientMaterial = board.isInsufficientMaterial();
        this.draw = board.isDraw();
        this.sideToMove = board.getSideToMove();
        this.fen = board.getFen();
    }

    public String getSan() {
        return san;
    }

    public Integer getMoveCounter() {
        return moveCounter;
    }

    public Integer getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public Boolean getMated() {
        return mated;
    }

    public Boolean getKingAttacked() {
        return kingAttacked;
    }

    public Boolean getStalemate() {
        return stalemate;
    }

    public Boolean getRepetition() {
        return repetition;
    }

    public Boolean getInsuffencientMaterial() {
        return insuffencientMaterial;
    }

    public Boolean getDraw() {
        return draw;
    }

    public Side getSideToMove() {
        return sideToMove;
    }

    public String getFen() {
        return fen;
    }

}