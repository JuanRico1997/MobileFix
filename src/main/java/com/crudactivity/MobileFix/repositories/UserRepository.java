package com.crudactivity.MobileFix.repositories;

import com.crudactivity.MobileFix.model.Role;
import com.crudactivity.MobileFix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

}
