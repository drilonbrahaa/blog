package com.example.blog.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.entities.Post;
import com.example.blog.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/author/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam String username) {
        try {
            postService.createPost(post, username);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/author/edit/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        try {
            Post post = postService.updatePost(id, updatedPost);
            return new ResponseEntity<>(post, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/author/delete/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable Long id) {
        if (postService.deletePost(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        List<Post> posts = postService.searchPosts(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/filter/category")
    public ResponseEntity<List<Post>> filterByCategory(@RequestParam String name) {
        List<Post> posts = postService.filterByCategory(name);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/filter/tags")
    public ResponseEntity<List<Post>> filterByTags(@RequestParam Set<String> tags) {
        List<Post> posts = postService.filterByTags(tags);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
