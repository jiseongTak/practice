package com.example.demo.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> searchBoardsByKeyword(String keyword, Pageable pageRequest) {
        log.info("BoardService searchBoardsByKeyword keyword >> {}", keyword);
        if (ObjectUtils.isEmpty(keyword)) {
            return boardRepository.findAll(pageRequest);
        } else {
            return boardRepository.findByTitleContaining(keyword, pageRequest);
        }
    }
}
