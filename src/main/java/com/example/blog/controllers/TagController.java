package com.example.blog.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/admin/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/create")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        try {
            Tag createdTag = tagService.createTag(tag);
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<Tag>> getTags() {
        List<Tag> tags = tagService.getTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Tag> getTag(@PathVariable Long id) {
        Tag tag = tagService.getTag(id);
        if (tag != null) {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        try {
            Tag updatedTag = tagService.updateTag(id, tag);
            return new ResponseEntity<>(updatedTag, HttpStatus.OK);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        if (tagService.deleteTag(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
