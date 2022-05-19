package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    Optional<UserRelationship> findByUserRelationship(Long emailId, Long friendId);
}
