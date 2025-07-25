package com.example.blog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blog.entities.Tag;
import com.example.blog.repositories.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag createTag(Tag tag) {
        if (tag.getName() == null || tag.getName().isEmpty()) {
            throw new RuntimeException("No tag specified!");
        }
        return tagRepository.save(tag);
    }

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Tag updateTag(Long id, Tag tag) {
        Tag updatedtag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("tag not found!"));
        if (tag.getName() != null && !tag.getName().isEmpty()) {
            updatedtag.setName(tag.getName());
        }
        return tagRepository.save(updatedtag);
    }

    public boolean deleteTag(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            tagRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
