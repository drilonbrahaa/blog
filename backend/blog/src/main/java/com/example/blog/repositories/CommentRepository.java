package com.example.blog.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.entities.Comment;
import com.example.blog.entities.Post;

// Comment repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findByPostId(Long postId);

    Set<Comment> findByAuthorId(Long authorId);

    List<Comment> findByAuthorUsername(String authorUsername);

    void deleteByPost(Post post);

    void deleteByAuthorId(Long authorId);
}
