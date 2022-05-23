package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "select u.email \n" +
            "from user_relationship ur right join public.\"user\" u on ur.friend_id = u.id\n" +
            "where ur.email_id = ?1 and ur.status = 'FRIEND'", nativeQuery = true)
    List<String> getListFriendEmails(Long id);

    @Query(value = "select f1.email \n" +
            "from (select id, email from user_relationship ur right join \"user\" u on ur.friend_id = u.id where ur.email_id = ?1 and status = 'FRIEND') as f1 \n" +
            "inner join (select id from user_relationship ur right join \"user\" u on ur.friend_id = u.id where ur.email_id = ?2 and status = 'FRIEND') as f2 \n" +
            "on f1.id = f2.id;", nativeQuery = true)
    List<String> getCommonFriends(long first_id, long second_id);
}
