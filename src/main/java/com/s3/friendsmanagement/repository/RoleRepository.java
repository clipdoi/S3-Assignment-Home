package com.s3.friendsmanagement.repository;


import com.s3.friendsmanagement.model.Role;
import com.s3.friendsmanagement.utils.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
