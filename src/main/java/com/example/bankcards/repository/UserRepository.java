package com.example.bankcards.repository;

import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID>, PagingAndSortingRepository<User, UUID> {
    @EntityGraph(value = "userWithRoles")
    Optional<User> findByLogin(String login);

    @Override
    @EntityGraph(value = "userWithRoles")
    Optional<User> findById(UUID uuid);

    @Query("select exists (from User u join u.roles r where r = :role)")
    boolean hasUsersWithRole(Role role);

    boolean existsByLoginOrEmail(String login, String email);
}
