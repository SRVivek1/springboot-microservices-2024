package com.srvivek.sboot.mservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srvivek.sboot.mservices.bean.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

}
