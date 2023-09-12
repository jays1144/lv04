package com.example.level04.controller;

import com.example.level04.dto.BoardRequestDto;
import com.example.level04.dto.BoardResponseDto;
import com.example.level04.entity.Board;
import com.example.level04.security.UserDetailsImpl;
import com.example.level04.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j(topic = "boardController")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards")
    public List<Board> getBoards(){
        return boardService.getBoards();
    }

    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> create(@RequestBody BoardRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info(requestDto.getTitle());
        log.info(userDetails.getUsername());
        BoardResponseDto responseDto = boardService.create(requestDto,userDetails.getUser());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/board/{id}")
    public Board getBoardBykey(@PathVariable Long id){
        return boardService.getBoardByKey(id);
    }

    @PutMapping("/board/{id}")
    public Board update(@RequestBody BoardRequestDto requestDto, @PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
       return boardService.update(requestDto,id, userDetails.getUser());
    }

    @DeleteMapping("/board/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return boardService.delete(id,userDetails.getUser());
    }
}
