package com.srvivek.sboot.mservices.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srvivek.sboot.mservices.bean.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
