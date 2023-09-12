package com.example.level04.service;

import com.example.level04.dto.BoardRequestDto;
import com.example.level04.dto.BoardResponseDto;
import com.example.level04.entity.Board;
import com.example.level04.entity.User;
import com.example.level04.repository.BoardRepository;
import com.example.level04.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Slf4j(topic = "boardService")
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

    public Board getBoardByKey(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional
    public Board update(BoardRequestDto requestDto, Long id, User user) {
        if(user.getRole().toString().equals("ADMIN")) {
            log.info("admin 입장");
            Board board = boardRepository.findById(id).orElseThrow();

            board.update(requestDto,id,user);
            return board;
        }
            Board board = findBoard(id, user.getUsername());
            board.update(requestDto, id, user);
            return board;

    }

    public String delete(Long id, User user) {
        if(user.getRole().toString().equals("ADMIN")){
            log.info("admin입장");
            Board board = boardRepository.findById(id).orElseThrow();
            boardRepository.delete(board);
            return "삭제 완료";
        }

        Board board = findBoard(id, user.getUsername());
        boardRepository.delete(board);

        return "삭제 완료";
    }

    private Board findBoard(Long id, String user){
        return boardRepository.findByIdAndUsername(id,user);
    }


}
