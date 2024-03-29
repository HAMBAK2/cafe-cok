package com.sideproject.hororok.category.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.keword.entity.Keyword;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Keyword> keywords;
}
