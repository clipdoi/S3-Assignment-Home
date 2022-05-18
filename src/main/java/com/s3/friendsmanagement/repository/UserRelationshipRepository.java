package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {
}
