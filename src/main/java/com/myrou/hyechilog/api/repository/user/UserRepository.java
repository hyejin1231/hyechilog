package com.myrou.hyechilog.api.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.myrou.hyechilog.api.domain.blog.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
	Optional<User> findByEmailAndPassword(String email, String password);
	
	Optional<User> findByEmail(String email);
}
