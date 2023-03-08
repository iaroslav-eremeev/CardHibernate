package com.iaroslaveremeev.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@RequiredArgsConstructor

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(nullable = false)
    @NonNull
    private String name;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Card> cardList = new ArrayList<>();

    public void addCard(Card card){
        this.cardList.add(card);
    }
}
