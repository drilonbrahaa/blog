package com.example.blog.mappers;

import com.example.blog.DTOs.tag.TagResponse;
import com.example.blog.entities.Tag;

public class TagMapper {
    public static TagResponse toDTO(Tag tag) {
        TagResponse tagDTO = new TagResponse();
        tagDTO.setId(tag.getId());
        tagDTO.setName(tag.getName());
        return tagDTO;
    }

    public static Tag toTagEntity(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }
}
