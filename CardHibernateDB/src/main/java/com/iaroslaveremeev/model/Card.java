package com.iaroslaveremeev.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@RequiredArgsConstructor

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(nullable = false)
    @NonNull
    private String question;
    @Column(nullable = false)
    @NonNull
    private String answer;
    @ManyToOne
    @JsonIgnore
    @NonNull
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column
    @NonNull
    private Date creationDate = new Date();
}
