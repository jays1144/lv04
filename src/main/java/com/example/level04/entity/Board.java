package com.example.level04.entity;

import com.example.level04.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    public Board(BoardRequestDto requestDto, String username) {
        this.username = username;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void update(BoardRequestDto requestDto, Long id, User user) {
        this.username = user.getUsername();
        this.title = requestDto.getTitle();
        this.id = id;
        this.content = requestDto.getContent();
    }

}
