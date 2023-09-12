package com.example.level04.service;

import com.example.level04.dto.BoardRequestDto;
import com.example.level04.dto.BoardResponseDto;
import com.example.level04.entity.Board;
import com.example.level04.entity.User;
import com.example.level04.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getBoards() {
        return boardRepository.findAllByOrderByCreateAtDesc();
    }

    public BoardResponseDto create(BoardRequestDto requestDto, User user) {
        Board board = new Board(requestDto,user.getUsername());
        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }
}
