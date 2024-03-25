package com.sideproject.hororok.Menu.entity;

import com.sideproject.hororok.cafe.entity.Cafe;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;
}
