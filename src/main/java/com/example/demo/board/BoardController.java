package com.example.demo.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        int blockPageSize = 5; // 한 블록에 보여질 페이지 번호 개수
        int blockNumber = page / blockPageSize; // 현재 블록 번호

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Board> pagedFindList = boardService.searchBoardsByKeyword(keyword, pageRequest);

        int totalPages = pagedFindList.getTotalPages(); // 전체 페이지 개수
        int endPageNumber = (totalPages - 1) / pageSize;

        // 다음 페이징 블록의 시작 페이지 번호 계산
        int nextStartPageNumber = (endPageNumber / blockPageSize + 1) * blockPageSize;

        // 현재 페이징 블록의 시작 페이지 번호 계산
        int startPageNumber = (page / blockPageSize) * blockPageSize;

        // 이전 페이징 블록의 마지막 페이지 번호 계산
        int previousEndPageNumber = startPageNumber < 1 ? 0 : startPageNumber - 1;

        // 이전 페이징 블록으로 이동하는 링크 생성
        String previousPageLink = "/list?page=" + previousEndPageNumber;

        // 다음 페이징 블록으로 이동하는 링크 생성
        String nextPageLink = "/list?page=" + nextStartPageNumber;

        log.info("BoardController list {}", pagedFindList.getTotalPages());

        model.addAttribute("end", blockNumber * blockPageSize + 4);
        model.addAttribute("start", blockNumber * blockPageSize);
        model.addAttribute("nextPageLink", nextPageLink);
        model.addAttribute("previousPageLink", previousPageLink);
        model.addAttribute("list", pagedFindList);
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
