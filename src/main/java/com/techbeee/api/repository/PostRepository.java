package com.techbeee.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techbeee.api.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

	public boolean existsByTitle(String title);
}
