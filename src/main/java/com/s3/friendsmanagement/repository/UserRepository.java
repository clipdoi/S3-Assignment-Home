package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

//    @Query(value = "SELECT id, email\n" +
//            "FROM user\n" +
//            "WHERE email = ?1", nativeQuery = true)
    User findByEmail(String email);

    @Query(value = "select u.email \n" +
            "from user_relationship ur right join public.\"user\" u on ur.friend_id = u.id\n" +
            "where ur.email_id = ?1 and ur.status = 'FRIEND'", nativeQuery = true)
    List<String> getListFriendEmails(Long id);


}
