package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipId> {

    @Query(value = "SELECT email_id, friend_id, status\n" +
            "FROM user_relationship\n" +
            "WHERE email_id = ?1 AND friend_id = ?2", nativeQuery = true)
    Optional<UserRelationship> findByUserRelationship(Long emailId, Long friendId);

    @Modifying
    @Query(value = "UPDATE public.user_relationship\n" +
            "SET status='BLOCK'\n" +
            "WHERE email_id= ?1 AND friend_id= ?2 AND status = 'FRIEND'", nativeQuery = true)
    int updateStatusByEmailIdAndFriendId(Long emailId, Long friendId);
}
