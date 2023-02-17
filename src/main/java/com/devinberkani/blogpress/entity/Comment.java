package com.devinberkani.blogpress.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // guarantees that all fields are initialized
@Builder
// allows for builder method to be used (User user = User.builder().name("John").age(30).email("john@example.com").build();)
@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(columnDefinition = "longtext")
    private String content;
    @CreationTimestamp
    private LocalDateTime createdOn;
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne // many comments belong to one post
    @JoinColumn(name="post_id", nullable = false) // column that is used to hold the foreign keys for the post entity
    private Post post;

}
