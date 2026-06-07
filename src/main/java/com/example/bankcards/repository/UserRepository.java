package com.example.bankcards.repository;

import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    @EntityGraph(value = "userWithRoles")
    Optional<User> findByLogin(String login);

    @Override
    @EntityGraph(value = "userWithRoles")
    Optional<User> findById(UUID uuid);

    boolean existsByLoginOrEmail(String login, String email);
}
