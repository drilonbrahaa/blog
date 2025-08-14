package com.example.blog.controllers;

import java.util.List;

import com.example.blog.DTOs.tag.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.entities.Tag;
import com.example.blog.services.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @PostMapping("/admin")
    public ResponseEntity<TagResponse> createTag(@RequestBody Tag tag) {
        TagResponse createdTag = tagService.createTag(tag);
        return ResponseEntity.status(201).body(createdTag);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long id, @RequestBody String newName) {
        TagResponse updatedTag = tagService.updateTag(id, newName);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}
