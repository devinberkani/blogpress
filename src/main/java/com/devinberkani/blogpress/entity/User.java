package com.devinberkani.blogpress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    // eager makes it so that when users are retrieved from the database their corresponding roles will always be loaded simultaneously
    // cascade all means whatever action we apply on user entity we will also apply on role entity, which makes sense because for example if you remove a user you want to remove their roles
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, // creates join column for PRIMARY KEY from user (id)
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")} // creates join column for PRIMARY KEY from role (id)
    )
    private List<Role> roles = new ArrayList<>();

//    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE) // mappedBy post indicates that it is the post column from Comments entity that maps to this post entity. The cascade = CascadeType.REMOVE attribute specifies that when a Post entity is removed, all related Comment entities should also be removed (cascaded deletion).
//    private Set<Post> posts = new HashSet<>();

}
