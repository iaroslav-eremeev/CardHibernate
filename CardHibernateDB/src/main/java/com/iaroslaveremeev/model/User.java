package com.iaroslaveremeev.model;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"login"})})
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login", nullable = false, unique = true)
    @NonNull
    private String login;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "password", nullable = false)
    @NonNull
    private String password;

    @Column(name = "regDate")
    @NonNull
    private Date regDate = new Date();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<Category> categoryList = new ArrayList<>();

    public void addCategory(Category category){
        this.categoryList.add(category);
    }
}

