package com.golden98.chess.ChessAPI;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveList;

@Component
public class GreedyEngine {

    private int[] pointValues (Board board) {
        String fen = board.getFen();
        fen = fen.substring(0, fen.indexOf(" ")); // remove info about 
        int black = 0;
        int white = 0;
        for (int i = 0; i < fen.length(); i++) {
            switch (fen.charAt(i)) {
                case 'p':
                    black += 1;
                case 'n':
                case 'b':
                    black += 3;
                case 'r':
                    black += 5;
                case 'q':
                    black += 9;
                case 'P':
                    white += 1;
                case 'N':
                case 'B':
                    white += 3;
                case 'R':
                    white += 5;
                case 'Q':
                    white += 9;
            }
        }
        return new int[] {white, black};
    }

    public Move getMove (Board board) {
        MoveList bestMoves = new MoveList();
        int pointMin = Integer.MAX_VALUE;
        // minimize opponents points
        int opponent = board.getSideToMove() == Side.WHITE ? 1 : 0;
        for (Move move : board.legalMoves()) {
            board.doMove(move);
            int[] points = pointValues(board);
            // minimize black points for white
            if (pointMin < points[opponent]) {
                pointMin = points[opponent];
                bestMoves = new MoveList();
                bestMoves.add(move);
            } else if (pointMin == points[opponent]) {
                bestMoves.add(move);
            }
            board.undoMove();
        }
        
        // pick random move from best
        int randomNum = ThreadLocalRandom.current().nextInt(0, bestMoves.size());
        return bestMoves.get(randomNum);
    }

    public void doBestMove (ChessGame chessGame) {
        chessGame.doMove(this.getMove(chessGame.getBoard()));
    }
}