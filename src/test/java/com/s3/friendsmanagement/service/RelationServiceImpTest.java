package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.exception.DataNotFoundException;
import com.s3.friendsmanagement.exception.StatusException;
import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.repository.UserRelationshipRepository;
import com.s3.friendsmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest()
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationServiceImpTest {

    @Autowired
    private RelationService relationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserRelationshipRepository relationshipRepository;

    private EmailRequest emailRequest;
    private CreateFriendConnectionReq addCommonRequest;
    private SubscribeAndBlockRequest subBlockRequest;
    private RetrieveRequest retrieveRequest;

    private List<String> listEmail;

    private User emailTest1;
    private User emailTest2;
    private User emailTest3;

    private UserRelationship friendRelationship1;
    private UserRelationship blockRelationship1;
    private UserRelationship subscribeRelationship1;

    private DataNotFoundException dataNotFoundException;
    private StatusException statusException;

    @BeforeAll
    public void setUp() {
        emailRequest = new EmailRequest();
        addCommonRequest = new CreateFriendConnectionReq();
        subBlockRequest = new SubscribeAndBlockRequest();
        retrieveRequest = new RetrieveRequest();

        emailTest1 = User.builder().id(1L).email("hongson@gmail.com").build();
        emailTest2 = User.builder().id(2L).email("minhthong@gmail.com").build();
        emailTest3 = User.builder().id(3L).email("saomai@gmail.com").build();

        listEmail = new ArrayList<>();

    }

    @Test
    public void testRetrieveFriendsListSuccess() {

        emailRequest = new EmailRequest(emailTest1.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);

        listEmail = Arrays.asList(emailTest2.getEmail(), emailTest3.getEmail());

        when(userRepository.getListFriendEmails(emailTest1.getId())).thenReturn(listEmail);

        List<String> listResults = relationService.retrieveFriendsList(emailRequest);

        assertEquals(listEmail, listResults);
    }

}
