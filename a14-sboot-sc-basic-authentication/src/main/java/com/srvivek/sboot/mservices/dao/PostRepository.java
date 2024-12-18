package com.srvivek.sboot.mservices.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srvivek.sboot.mservices.bean.Post;
import com.srvivek.sboot.mservices.bean.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	/**
	 * Find post for the given user with post id.
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public Optional<Post> findByIdAndUser(Integer id, User user);
}
