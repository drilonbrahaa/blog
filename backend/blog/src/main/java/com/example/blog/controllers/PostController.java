package com.example.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.DTOs.post.PostRequest;
import com.example.blog.DTOs.post.PostResponse;
import com.example.blog.services.PostService;

@RestController
@RequestMapping("/api/posts")
// Controller for managing blog posts
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author")
    public List<PostResponse> getPostsByAuthor(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject(); // username from token
        return postService.getPostsByAuthor(username);
    }


    @GetMapping("/title")
    public ResponseEntity<List<PostResponse>> getPostsByTitle(@RequestParam String title) {
        List<PostResponse> posts = postService.getPostsByTitle(title);
        if (posts != null && !posts.isEmpty()) {
            return ResponseEntity.ok(posts);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/author")
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal Jwt jwt, @RequestBody PostRequest postRequest) {
        String username = jwt.getSubject();
        try {
            PostResponse createdPost = postService.createPost(username, postRequest);
            return ResponseEntity.status(201).body(createdPost);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        try {
            PostResponse updatedPost = postService.updatePost(id, postRequest);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }
}
