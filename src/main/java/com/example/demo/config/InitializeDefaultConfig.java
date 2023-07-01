package com.example.demo.config;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InitializeDefaultConfig {

    private final BoardRepository boardRepository;

    @Bean
    public void initializeDefaultBoard() {
        for (int i = 0; i < 50; i++) {
            Board board = new Board("test" + i, "content test" + i);
            boardRepository.save(board);
        }
    }
}
