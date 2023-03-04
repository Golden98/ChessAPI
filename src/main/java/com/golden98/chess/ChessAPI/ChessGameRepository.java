package com.golden98.chess.ChessAPI;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessGameRepository extends JpaRepository<ChessGame, Long>{
    
}