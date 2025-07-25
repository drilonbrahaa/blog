package com.example.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.blog.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
