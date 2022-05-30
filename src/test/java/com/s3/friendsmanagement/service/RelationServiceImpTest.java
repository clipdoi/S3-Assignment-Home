package com.s3.friendsmanagement.service;

import com.s3.friendsmanagement.exception.DataNotFoundException;
import com.s3.friendsmanagement.exception.StatusException;
import com.s3.friendsmanagement.model.User;
import com.s3.friendsmanagement.model.UserRelationship;
import com.s3.friendsmanagement.model.UserRelationshipId;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.repository.UserRelationshipRepository;
import com.s3.friendsmanagement.repository.UserRepository;
import com.s3.friendsmanagement.utils.EStatus;
import com.s3.friendsmanagement.utils.ErrorConstraints;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private User emailTest4;
    private User emailTest5;
    private User emailTest6;

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
        emailTest4 = User.builder().id(6L).email("ngoctu@gmail.com").build();
        emailTest5 = User.builder().id(4L).email("nguyenquang@gmail.com").build();
        emailTest6 = User.builder().id(5L).email("kienca@gmail.com").build();

        friendRelationship1 = UserRelationship.builder().id(UserRelationshipId.builder().emailId(1L).friendId(2L).status(EStatus.FRIEND.name()).build()).build();
        blockRelationship1 = UserRelationship.builder().id(UserRelationshipId.builder().emailId(5L).friendId(4L).status(EStatus.BLOCK.name()).build()).build();
        subscribeRelationship1 = UserRelationship.builder().id(UserRelationshipId.builder().emailId(6L).friendId(5L).status(EStatus.SUBSCRIBE.name()).build()).build();


        listEmail = new ArrayList<>();

        dataNotFoundException = new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND);

    }

    @Test
    public void testAddFriendSuccess() {
        addCommonRequest = new CreateFriendConnectionReq
                (Arrays.asList(emailTest1.getEmail(), emailTest2.getEmail()));

        when(userRepository.findByEmail(emailTest1.getEmail()))
                .thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail()))
                .thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship
                (emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.empty());

        Boolean result = relationService.addFriend(addCommonRequest);
        assertSame(true, result);
    }


    @Test
    public void testAddFriendNotExistEmail() {
        addCommonRequest = new CreateFriendConnectionReq(Arrays.asList("son1@gmail.com", "son2@gmail.com"));

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.addFriend(addCommonRequest)));
    }

    @Test
    public void testAddFriendBlockedEmail() {
        statusException = new StatusException("This email has been blocked !");
        addCommonRequest = new CreateFriendConnectionReq(Arrays.asList(emailTest1.getEmail(), emailTest2.getEmail()));

        when(userRepository.findByEmail(emailTest1.getEmail()))
                .thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail()))
                .thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship
                (emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(blockRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.addFriend(addCommonRequest)));
    }

    @Test
    public void testAddFriendOldFriend() {
        statusException = new StatusException("They have already being friend.");
        addCommonRequest = new CreateFriendConnectionReq(Arrays.asList(emailTest1.getEmail(), emailTest2.getEmail()));

        when(userRepository.findByEmail(emailTest1.getEmail()))
                .thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail()))
                .thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship
                (emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(friendRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.addFriend(addCommonRequest)));

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

    @Test
    public void testRetrieveFriendsListNotExistEmail() {
        emailRequest = new EmailRequest(emailTest1.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(null);

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.retrieveFriendsList(emailRequest)));

    }

    @Test
    public void testGetCommonSuccess() {
        addCommonRequest = new CreateFriendConnectionReq
                (Arrays.asList(emailTest2.getEmail(), emailTest3.getEmail()));
        listEmail = Collections.singletonList(emailTest1.getEmail());

        when(userRepository.findByEmail(emailTest2.getEmail()))
                .thenReturn(emailTest2);
        when(userRepository.findByEmail(emailTest3.getEmail()))
                .thenReturn(emailTest3);
        when(userRepository.getCommonFriends(emailTest2.getId(), emailTest3.getId()))
                .thenReturn(listEmail);

        List<String> list = relationService.getCommonFriends(addCommonRequest);
        assertEquals(listEmail, list);

    }

    @Test
    public void testGetCommonNotExistEmail() {
        addCommonRequest = new CreateFriendConnectionReq(Arrays.asList("son1@gmail.com", "son2@gmail.com"));

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.getCommonFriends(addCommonRequest)));
    }

    @Test
    public void testSubscribeSuccess() {
        subBlockRequest = new SubscribeAndBlockRequest(emailTest6.getEmail(), emailTest5.getEmail());

        when(userRepository.findByEmail(emailTest6.getEmail())).thenReturn(emailTest6);
        when(userRepository.findByEmail(emailTest5.getEmail())).thenReturn(emailTest5);
        when(relationshipRepository.findByUserRelationship(emailTest6.getId(), emailTest5.getId()))
                .thenReturn(Optional.empty());

        assertSame("true", relationService.subscribeTo(subBlockRequest).toString());
    }

    @Test
    public void testSubscribeNotExistEmail() {
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(null);

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.subscribeTo(subBlockRequest)));
    }

    @Test
    public void testSubscribeBlockedEmail() {
        statusException = new StatusException("Target email has been blocked !");
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail())).thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship(emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(blockRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.subscribeTo(subBlockRequest)));

    }

    @Test
    public void testSubscribedEmail() {
        statusException = new StatusException("Already subscribed to this target email !");
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail())).thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship(emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(subscribeRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.subscribeTo(subBlockRequest)));
    }

    @Test
    public void testSubscribeFriendsEmail() {
        statusException = new StatusException("Already being friend of this target ,no need to subscribe !");
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail())).thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship(emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(friendRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.subscribeTo(subBlockRequest)));
    }

    @Test
    public void testBlockEmailSuccess() {
        subBlockRequest = new SubscribeAndBlockRequest(emailTest5.getEmail(), emailTest4.getEmail());

        when(userRepository.findByEmail(emailTest5.getEmail())).thenReturn(emailTest5);
        when(userRepository.findByEmail(emailTest4.getEmail())).thenReturn(emailTest4);
        when(relationshipRepository.findByUserRelationship(emailTest5.getId(), emailTest4.getId()))
                .thenReturn(Optional.empty());
        when(relationshipRepository.updateStatusByEmailIdAndFriendId(emailTest5.getId(), emailTest4.getId()))
                .thenReturn(1);

        assertSame("true", relationService.blockEmail(subBlockRequest).toString());
    }

    @Test
    public void testBlockNotExistEmail() {
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(null);

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.blockEmail(subBlockRequest)));

    }

    @Test
    public void testBlockBlockedEmail() {
        statusException = new StatusException("This email has already being blocked !");
        subBlockRequest = new SubscribeAndBlockRequest(emailTest1.getEmail(), emailTest2.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest2.getEmail())).thenReturn(emailTest2);
        when(relationshipRepository.findByUserRelationship(emailTest1.getId(), emailTest2.getId()))
                .thenReturn(Optional.ofNullable(blockRelationship1));

        assertEquals(statusException, assertThrows(StatusException.class
                , () -> relationService.blockEmail(subBlockRequest)));

    }

    @Test
    public void testRetrieveEmailSuccess() {
        retrieveRequest = new RetrieveRequest(emailTest1.getEmail(), "Hello " + emailTest5.getEmail());
        listEmail = Arrays.asList(emailTest2.getEmail(), emailTest3.getEmail(), emailTest5.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(emailTest1);
        when(userRepository.findByEmail(emailTest5.getEmail())).thenReturn(emailTest5);
        when(userRepository.getRetrievableEmail(any())).thenReturn(listEmail);

        Set<String> setEmails = relationService.retrieveEmails(retrieveRequest);

        assertEquals(new HashSet<>(listEmail), setEmails);

    }

    @Test
    public void testRetrieveEmailNotExist() {
        retrieveRequest = new RetrieveRequest(emailTest1.getEmail(), "Hello " + emailTest5.getEmail());
        listEmail = Arrays.asList(emailTest2.getEmail(), emailTest3.getEmail(), emailTest5.getEmail());

        when(userRepository.findByEmail(emailTest1.getEmail())).thenReturn(null);

        assertEquals(dataNotFoundException, assertThrows(DataNotFoundException.class
                , () -> relationService.retrieveEmails(retrieveRequest)));

    }

}
