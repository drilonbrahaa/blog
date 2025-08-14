package com.example.blog.services;

import java.util.List;
import java.util.Set;

import com.example.blog.DTOs.tag.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.blog.mappers.TagMapper;
import com.example.blog.entities.Tag;
import com.example.blog.repositories.TagRepository;

import static com.example.blog.mappers.TagMapper.toDTO;


@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<TagResponse> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        return tags.stream().map(TagMapper::toDTO).toList();
    }

    public List<TagResponse> getTagsByNames(Set<String> names) {
        Set<Tag> tags = tagRepository.findAllByNameIn(names);
        return tags.stream().map(TagMapper::toDTO).toList();
    }

    public TagResponse getTagByName(String name) {
        Tag tag = tagRepository.findByName(name).orElseThrow(() -> new RuntimeException("Tag not found!"));
        return toDTO(tag);
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    public TagResponse createTag(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new RuntimeException("Tag already exists");
        }
        tagRepository.save(tag);
        return toDTO(tag);
    }

    public TagResponse updateTag(Long id, String newName) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        if (tagRepository.findByName(newName).isPresent()) {
            throw new RuntimeException("Tag with this name already exists");
        }
        if (newName != null && !newName.isEmpty()) {
            existingTag.setName(newName);
        }
        tagRepository.save(existingTag);
        return toDTO(existingTag);
    }

    public void deleteTag(Long id) {
        if (tagRepository.existsById(id)) {
            tagRepository.deleteById(id);
        }
    }
}
