package com.example.level04.repository;

import com.example.level04.dto.BoardResponseDto;
import com.example.level04.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByOrderByCreateAtDesc();
}
