package org.example.basic.repositories;

import org.example.basic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUserName(String userName);
}
