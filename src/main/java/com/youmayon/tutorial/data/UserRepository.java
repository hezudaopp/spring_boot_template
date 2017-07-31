package com.youmayon.tutorial.data;

import com.youmayon.tutorial.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findByEnabled(boolean enabled);

    @Query("from User u where u.username=:username")
    User findUser(@Param("username") String username);
}
