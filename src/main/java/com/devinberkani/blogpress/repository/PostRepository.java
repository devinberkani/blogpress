package com.devinberkani.blogpress.repository;

import com.devinberkani.blogpress.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

//@Repository annotation is not needed because SimpleJpaRepository class is already annotated with Repository
public interface PostRepository extends JpaRepository<Post, Long> { // <Entity Type, Primary ID Type>
    // JpaRepository recognizes the pattern "findBy" followed by the name of a property in the entity (Post), and would generate a query to find a Post entity by its url property.
    Optional<Post> findByUrl(String url);

    // this query checks title and short description for substrings of %query% where % is a wildcard
    // searches ALL posts
    @Query(value = "SELECT p from Post p WHERE " +
            " p.title LIKE CONCAT('%', :query, '%') OR " +
            " p.shortDescription LIKE CONCAT('%', :query, '%')",
            countQuery = "SELECT count(*) from Post p WHERE " +
                    " p.title LIKE CONCAT('%', :query, '%') OR " +
                    " p.shortDescription LIKE CONCAT('%', :query, '%')")
    Page<Post> searchPosts(String query, Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.created_by =:userId", nativeQuery = true)
    List<Post> findPostsByUser(Long userId);

    // searches posts based on user id
    @Query(value = "SELECT p FROM Post p WHERE " +
            " p.createdBy.id = :userId AND " +
            " (p.title LIKE CONCAT('%', :query, '%') OR " +
            " p.shortDescription LIKE CONCAT('%', :query, '%'))",
            countQuery = "SELECT p FROM Post p WHERE " +
                    " p.createdBy.id = :userId AND " +
                    " (p.title LIKE CONCAT('%', :query, '%') OR " +
                    " p.shortDescription LIKE CONCAT('%', :query, '%'))")
    Page<Post> searchPostsByUserId(String query, Long userId, Pageable pageable);

}
