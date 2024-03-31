package com.techbeee.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.techbeee.api.dto.PostDto;
import com.techbeee.api.dto.PostResponse;
import com.techbeee.api.model.Post;
import com.techbeee.api.service.PostService;
import com.techbeee.api.utils.AppConstants;

@RestController
@RequestMapping("/api/posts")
public class PostController {

	@Autowired
	private PostService service;
	
	@GetMapping
	public ResponseEntity<PostResponse> showAllPosts(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
													  @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
													  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
													  @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULY_SORT_DIRECTION, required = false) String sortDir) {
		PostResponse response= service.AllPosts(pageNo, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PostDto> showPostById(@PathVariable int id) {
		PostDto post = service.getPostById(id);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PostDto> savePost(@RequestBody Post post) {
		PostDto dto = service.addPost(post);
		return new ResponseEntity<PostDto>(dto, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostDto> updatePost(@PathVariable int id, @RequestBody Post post) {
		PostDto dto = service.updatePostById(id, post);
		return new ResponseEntity<PostDto>(dto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePost(@PathVariable int id) {
		service.deletePostById(id);
		return new ResponseEntity<String>("Post Deleted Successfully!", HttpStatus.OK);
	}
}
