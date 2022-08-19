package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

// 기본적인 CRUD 함수를 들고있음
// @Repository 없어도 자동으로 IoC된다. JpaRepository상속을 했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByUsername(String username);
}
