package com.example.demo.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @RequestMapping("/")
    public String index() {
        return "hello";
    }

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        int pageSize = 8; // 페이지당 아이템 개수

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> pagedFindList = boardService.searchBoardsByKeyword(keyword, pageable);

        model.addAttribute("list", pagedFindList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        model.addAttribute("keyword", keyword);

        return "list";
    }

    @GetMapping("/save")
    public String savePage() {
        return "save";
    }

    @Transactional
    @PostMapping("/save")
    public String save(Board board) {
        boardRepository.save(board);
        return "redirect:/list";
    }

    @GetMapping("/board")
    public String board(@RequestParam Long id, Model model) {
        Board board = boardRepository.findById(id).orElseThrow();
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/modify")
    public String modifyPage(@RequestParam Long id, Model model) {
        Board board = boardRepository.findById(id).orElseThrow();
        model.addAttribute("board", board);
        return "modify";
    }

    @Transactional
    @PostMapping("/update")
    public String update(Board board) {
        Board findBoard = boardRepository.findById(board.getId()).orElseThrow();
        findBoard.updateTo(board);
        return "redirect:/list";
    }

    @Transactional
    @PostMapping("/remove")
    public String remove(@RequestParam Long id) {
        Board board = boardRepository.findById(id).orElseThrow();
        boardRepository.delete(board);
        return "redirect:/list";
    }

}
