package com.s3.friendsmanagement.repository;

import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRelationshipRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRelationshipRepository relationshipRepository;

    private UserRelationship relationTest;
    private List<UserRelationship> lisRelations;
    private UserRelationshipId userRelationshipId;

    @BeforeAll
    public void setup(){
        userRelationshipId = UserRelationshipId.builder().emailId(1L).friendId(4L).build();
        relationTest = UserRelationship.builder().id(userRelationshipId).build();
    }

    @Test
    public void findByUserRelationship(){
         Optional<UserRelationship> relation = relationshipRepository.findByUserRelationship
                (relationTest.getId().getEmailId(), relationTest.getId().getFriendId());

        assertEquals(relationTest.getId().getEmailId(), relation.get().getId().getEmailId());
    }

    @Test
    public void updateStatusByEmailIdAndFriendId(){
        int row = relationshipRepository.updateStatusByEmailIdAndFriendId
                (relationTest.getId().getEmailId(), relationTest.getId().getFriendId());
        assertEquals(1, row);
    }


}
