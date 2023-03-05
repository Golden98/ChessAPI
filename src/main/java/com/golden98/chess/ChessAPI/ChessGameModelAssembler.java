package com.golden98.chess.ChessAPI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import com.github.bhlangonijr.chesslib.move.Move;


@Component
public class ChessGameModelAssembler implements RepresentationModelAssembler<ChessGame, EntityModel<SimpleChessGame>>{



    @Override
    public EntityModel<SimpleChessGame> toModel (ChessGame chessGame) {
        EntityModel<SimpleChessGame> model = EntityModel.of(new SimpleChessGame(chessGame),
        linkTo(methodOn(ChessGameController.class).one(chessGame.getId())).withSelfRel());
        for (Move moveObj: chessGame.getBoard().legalMoves()) {
            String move = moveObj.toString();
            model.add(linkTo(methodOn(ChessGameController.class).move(chessGame.getId(), move)).withRel(move));
        }
        return model;
    }
}