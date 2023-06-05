package com.example.demo.board;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    private Long id;

    private String title;
    private String content;

    public void updateTo(Board board) {
        this.title = board.title;
        this.content = board.content;
    }
}
