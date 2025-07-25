package com.example.blog.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);

    List<Post> findByCategory_Name(String category);

    List<Post> findDistinctByTags_NameIn(Set<String> tags);
}
