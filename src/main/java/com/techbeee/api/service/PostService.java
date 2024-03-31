package com.techbeee.api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.techbeee.api.dto.PostDto;
import com.techbeee.api.dto.PostResponse;
import com.techbeee.api.exception.BadRequestException;
import com.techbeee.api.exception.NotFoundException;
import com.techbeee.api.model.Post;

@Service
public class PostService {

	@Autowired
	private com.techbeee.api.repository.PostRepository repository;
	
	public PostResponse AllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Post> posts = repository.findAll(pageable);
		if (posts.isEmpty()) {
			throw new NotFoundException("There are no posts!");
		}
		List<Post> allPosts = posts.getContent();
		List<PostDto> content =  allPosts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());
		PostResponse response = new PostResponse();
		response.setContent(content);
		response.setPageNo(posts.getNumber());
		response.setPageSize(posts.getSize());
		response.setTotalElements(posts.getTotalElements());
		response.setTotalPages(posts.getTotalPages());
		response.setLast(posts.isLast());
		
		return response;
		
	}
	
	public PostDto getPostById(int id) {
		Optional<Post> opt = repository.findById(id);
		if (opt.isEmpty()) {
			throw new NotFoundException("Post Not Found with this Id: "+id);
		}
		PostDto dto = mapToDto(opt.get());
		return dto;
	}
	
	public PostDto addPost(Post post) {
		if (repository.existsByTitle(post.getTitle())) {
			throw new BadRequestException("Post Title with this name already Exsits!");
		}
		repository.save(post);
		return mapToDto(post);
	}
	
	public PostDto updatePostById(int id, Post post) {
		Optional<Post> opt = repository.findById(id);
		if (opt.isEmpty()) {
			throw new NotFoundException("Post Not Found with this Id: "+id);
		}
		Post p = opt.get();
		p.setPostId(id);
		p.setTitle(post.getTitle());
		p.setDescription(post.getDescription());
		p.setContent(post.getContent());
		
		repository.save(p);
		return mapToDto(p);
	}
	
	public void deletePostById(int id) {
		Optional<Post> opt = repository.findById(id);
		if (opt.isEmpty()) {
			throw new NotFoundException("Post Not Found with this Id: "+id);
		}
		Post post = opt.get();
		repository.delete(post);
	}
	
	private PostDto mapToDto(Post post) {
		PostDto dto = new PostDto();
		dto.setId(post.getPostId());
		dto.setTitle(post.getTitle());
		dto.setDescription(post.getDescription());
		dto.setContent(post.getContent());
		
		return dto;
	}
}
