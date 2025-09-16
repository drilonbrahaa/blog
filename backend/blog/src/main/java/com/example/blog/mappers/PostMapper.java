package com.example.blog.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.blog.DTOs.post.PostRequest;
import com.example.blog.DTOs.post.PostResponse;
import com.example.blog.entities.Category;
import com.example.blog.entities.Post;
import com.example.blog.entities.Tag;

// Mapper for Post entity and DTO
public class PostMapper {
    public static PostResponse toDTO(Post post) {
        if (post == null) {
            return null;
        }
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setCategoryName(post.getCategory().getName());
        postResponse.setTagNames(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        postResponse.setAuthorUsername(post.getAuthor().getUsername());
        postResponse.setComments(post.getComments().stream().map(CommentMapper::toCommentDTO).toList());
        return postResponse;
    }

    public static Post toCrudEntity(PostRequest postRequest, Category category, Set<Tag> tags) {
        if (postRequest == null) {
            return null;
        }
        System.out.println(category.getName());
        for(Tag tag : tags) {
            System.out.println(tag.getName());
        }
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setCategory(category);
        post.setTags(tags);
        return post;
    }
}
