package com.devinberkani.blogpress.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// POSTS ASSOCIATED WITH PostRepository

// Lombok annotations to reduce boilerplate code
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // guarantees that all fields are initialized
@Builder // allows for builder method to be used (User user = User.builder().name("John").age(30).email("john@example.com").build();)
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) // can't be null - optional annotation that allows custom mapping between entity field and database column
    private String title;
    private String url;
    @Lob
    @Column(nullable = false, columnDefinition = "longtext")
    private String content;
    @Column(nullable = false, columnDefinition = "longtext")
    private String shortDescription;
    @CreationTimestamp // automatically populates value of field with created timestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp // automatically populates value of field with updated timestamp
    private LocalDateTime updatedOn;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE) // mappedBy post indicates that it is the post column from Comments entity that maps to this post entity. The cascade = CascadeType.REMOVE attribute specifies that when a Post entity is removed, all related Comment entities should also be removed (cascaded deletion).
    private Set<Comment> comments = new HashSet<>();
    @ManyToOne // many posts belong to one user
    @JoinColumn(name="created_by", nullable = false) // column that is used to hold the foreign keys for the post entity
    private User createdBy;

}
