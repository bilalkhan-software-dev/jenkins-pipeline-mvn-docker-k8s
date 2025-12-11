package com.practice.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "my_users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends AbstractEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 100, unique = true)
    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> another;

    public void addComment(Comment comment) {
        comment.setUser(this);
        another.add(comment);
    }

    public void removeComment(Comment comment) {
        comment.setUser(null);
        another.remove(comment);
    }


}
