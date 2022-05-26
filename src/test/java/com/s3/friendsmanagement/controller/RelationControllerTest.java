package com.s3.friendsmanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3.friendsmanagement.exception.DataNotFoundException;
import com.s3.friendsmanagement.exception.StatusException;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.repository.UserRelationshipRepository;
import com.s3.friendsmanagement.repository.UserRepository;
import com.s3.friendsmanagement.service.RelationService;
import com.s3.friendsmanagement.utils.ErrorConstraints;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc()
@EnableWebMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelationService relationService;

    @MockBean
    private UserRelationshipRepository relationshipRepository;

    @MockBean
    private UserRepository emailRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private EmailRequest emailRequest;
    private CreateFriendConnectionReq addCommonRequest;
    private SubscribeAndBlockRequest subBlockRequest;
    private RetrieveRequest retrieveRequest;

    List<String> listEmail;
    Set<String> setEmails;

    @BeforeAll
    public void setUp() {
        listEmail = new ArrayList<>();
        listEmail.add("saomai@gmail.com");
        listEmail.add("minhthong@gmail.com");

        setEmails = new HashSet<>();
        setEmails.add("saomai@gmail.com");
        setEmails.add("minhthong@gmail.com");

        retrieveRequest = new RetrieveRequest();
        emailRequest = new EmailRequest();
        addCommonRequest = new CreateFriendConnectionReq();
        subBlockRequest = new SubscribeAndBlockRequest();

    }

    @AfterAll
    public void clear() {
        retrieveRequest = new RetrieveRequest();
        emailRequest = new EmailRequest();
        addCommonRequest = new CreateFriendConnectionReq();
        subBlockRequest = new SubscribeAndBlockRequest();
    }

    @Test
    public void addFriendsSuccessTest() throws Exception {
        addCommonRequest.setFriends(Arrays.asList("abcxyz@gmail.com", "example@gmail.com"));
        //Test with success case
        String json = objectMapper.writeValueAsString(addCommonRequest);

        when(relationService.addFriend(any(CreateFriendConnectionReq.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true}", content);
    }

    @Test
    public void addFriendsInvalidRequestTest() throws Exception {
        //Test with invalid AddFriend request
        addCommonRequest.setFriends(null);

        when(relationService.addFriend(any(CreateFriendConnectionReq.class))).thenReturn(true);

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result = mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"List email must not be null or empty\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}"
                , content);
    }

    @Test
    public void addFriendsLackEmailRequestTest() throws Exception {
        //Test with invalid AddFriend request
        addCommonRequest.setFriends(Collections.singletonList("example@gmail.com"));
        String json = objectMapper.writeValueAsString(addCommonRequest);

        when(relationService.addFriend(any(CreateFriendConnectionReq.class))).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Request must contain two emails\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/add\"}"
                , content);
    }

    @Test
    public void addFriendsInvalidEmailTest() throws Exception {
        //Test with invalid AddFriend request
        addCommonRequest.setFriends(Arrays.asList("abcGmail.com", "deve@gmail.com"));

        when(relationService.addFriend(any(CreateFriendConnectionReq.class))).thenReturn(true);

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result = mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Invalid input email format\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/add\"}"
                , content);
    }

    @Test
    public void addFriendsSameEmailTest() throws Exception {
        //Test with invalid AddFriend request
        addCommonRequest.setFriends(Arrays.asList("abcd@gmail.com", "abcd@gmail.com"));
        when(relationService.addFriend(any(CreateFriendConnectionReq.class))).thenReturn(true);

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Input emails duplicated\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/add\"}"
                , content);
    }

    @Test
    public void addFriendsBlockEmailTest() throws Exception {
        //Test with blocked email
        addCommonRequest.setFriends(Arrays.asList("hongson@gmail.com", "nguyenquang@gmail.com"));
        when(relationService.addFriend(any(CreateFriendConnectionReq.class)))
                .thenThrow(new StatusException("This email has been blocked !"));

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"This email has been blocked !\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/add\"}"
                , content);
    }

    @Test
    public void addFriendsAlreadyFriendTest() throws Exception {
        //Test with already be friend email
        addCommonRequest.setFriends(Arrays.asList("hongson@gmail.com", "saomai@gmail.com"));

        when(relationService.addFriend(any(CreateFriendConnectionReq.class)))
                .thenThrow(new StatusException("Two Email have already being friend"));

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result = mockMvc.perform(post("/api/emails/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Two Email have already being friend\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/add\"}"
                , content);
    }

    @Test
    public void retrieveFriendsListSuccessTest() throws Exception {
        //Success test
        List<String> listEmail1 = new ArrayList<>();
        listEmail1.add("saomai@gmail.com");
        listEmail1.add("minhthong@gmail.com");
        emailRequest = new EmailRequest("hongson@gmail.com");
        String json = objectMapper.writeValueAsString(emailRequest);

        when(relationService.retrieveFriendsList(any(EmailRequest.class))).thenReturn(listEmail1);

        MvcResult result = mockMvc.perform(post("/api/emails/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true,\"friends\":[\"saomai@gmail.com\",\"minhthong@gmail.com\"],\"count\":2}", content);

    }

    @Test
    public void retrieveFriendsListNoContent() throws Exception {
        //Test with no content case
        emailRequest = new EmailRequest("kienca@gmail.com");
        String json = objectMapper.writeValueAsString(emailRequest);
        listEmail = Collections.emptyList();
        when(relationService.retrieveFriendsList(any(EmailRequest.class))).thenReturn(listEmail);

        mockMvc.perform(post("/api/emails/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @Test
    public void retrieveFriendsListInvalidRequestTest() throws Exception {
        //Test with invalid request
        emailRequest = new EmailRequest("");
        String json = objectMapper.writeValueAsString(emailRequest);

        MvcResult result = mockMvc.perform(post("/api/emails/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"Email mustn't be empty or null\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}", content);
    }

    @Test
    public void retrieveFriendsListInvalidEmailRequestTest() throws Exception {
        //Test with invalid email
        emailRequest = new EmailRequest("invalid");
        String json = objectMapper.writeValueAsString(emailRequest);

        when(relationService.retrieveFriendsList(any(EmailRequest.class))).thenReturn(listEmail);

        MvcResult result = mockMvc.perform(post("/api/emails/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Invalid input email format\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/friends\"}"
                , content);
    }

    @Test
    public void retrieveFriendsListEmailNotExistTest() throws Exception {
        //Test with not existed email
        emailRequest = new EmailRequest("nguyenkhoi@gmail.com");
        String json = objectMapper.writeValueAsString(emailRequest);

        when(relationService.retrieveFriendsList(any(EmailRequest.class)))
                .thenThrow(new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND));

        MvcResult result = mockMvc.perform(post("/api/emails/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":404,\"message\":\"Input email not found\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/friends\"}"
                , content);
    }

    @Test
    public void getCommonFriendsSuccessTest() throws Exception {
        //Test with success case
        addCommonRequest.setFriends(Arrays.asList("hongson@gmail.com", "kienca@gmail.com"));

        when(relationService.getCommonFriends(any(CreateFriendConnectionReq.class))).thenReturn(listEmail);

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true,\"friends\":[\"saomai@gmail.com\",\"minhthong@gmail.com\"],\"count\":2}", content);
    }

    @Test
    public void getCommonFriendsNoContent() throws Exception {
        //Test with no content case
        addCommonRequest.setFriends(Arrays.asList("hongson@gmail.com", "kienca@gmail.com"));
        listEmail = Collections.emptyList();
        when(relationService.getCommonFriends(any(CreateFriendConnectionReq.class))).thenReturn(listEmail);

        String json = objectMapper.writeValueAsString(addCommonRequest);
        mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void getCommonFriendsInvalidInputTest() throws Exception {
        //Test with invalid input
        addCommonRequest.setFriends(null);
        String json = objectMapper.writeValueAsString(addCommonRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"List email must not be null or empty\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}", content);

    }

    @Test
    public void getCommonFriendsLackEmailRequestTest() throws Exception {
        //Test with invalid request
        addCommonRequest.setFriends(Collections.singletonList("example@gmail.com"));
        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Request must contain two emails\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/common\"}"
                , content);
    }

    @Test
    public void getCommonFriendsInvalidEmailTest() throws Exception {
        //Test with invalid request
        addCommonRequest.setFriends(Arrays.asList("abcEmail.com", "example@gmail.com"));
        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Invalid input email format\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/common\"}"
                , content);
    }

    @Test
    public void getCommonFriendsSameEmailTest() throws Exception {
        //Test with invalid request
        addCommonRequest.setFriends(Arrays.asList("hongson@gmail.com", "hongson@gmail.com"));
        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Input emails duplicated\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/common\"}"
                , content);
    }

    @Test
    public void getCommonFriendsNotExistEmailTest() throws Exception {
        //Test with invalid commonFriend request
        addCommonRequest.setFriends(Arrays.asList("xxyyzz@gmail.com", "aabbcc@gmail.com"));

        when(relationService.getCommonFriends(any(CreateFriendConnectionReq.class)))
                .thenThrow(new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND));

        String json = objectMapper.writeValueAsString(addCommonRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/common")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":404,\"message\":\"Input email not found\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/common\"}"
                , content);
    }

    @Test
    public void subscribeToSuccessTest() throws Exception {
        //Test with success case
        subBlockRequest = new SubscribeAndBlockRequest
                ("hongson@gmail.com", "saomai@gmail.com");

        when(relationService.subscribeTo(any(SubscribeAndBlockRequest.class))).thenReturn(true);

        String json = objectMapper.writeValueAsString(subBlockRequest);
        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true}", content);
    }

    @Test
    public void subscribeToInvalidInputTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester(null);
        subBlockRequest.setTarget("target@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"Requester email must not be empty or null\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}", content);

    }

    @Test
    public void subscribeToInvalidEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("requesterGmail.com");
        subBlockRequest.setTarget("target@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Invalid input email format\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void subscribeToSameEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("hongson@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result = mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Input emails duplicated\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void subscribeToNotExistEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("xxyyzz@gmail.com");
        subBlockRequest.setTarget("target@gmail.com");

        when(relationService.subscribeTo(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":404,\"message\":\"Input email not found\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void subscribeToBlockedExistEmailTest() throws Exception {
        //Test with blocked email
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("nguyenquang@gmail.com");

        when(relationService.subscribeTo(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new StatusException("This email has been blocked !!"));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"This email has been blocked !!\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void subscribeToFriendEmailTest() throws Exception {
        //Test with already be friend email
        subBlockRequest.setRequester("anhthus@gmail.com");
        subBlockRequest.setTarget("huynhquang@gmail.com");

        when(relationService.subscribeTo(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new StatusException("Already being friend of this target !"));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Already being friend of this target !\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void subscribeToSubscribedEmailTest() throws Exception {
        //Test with subscribed email
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("phamquan@gmail.com");

        when(relationService.subscribeTo(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new StatusException("Already subscribed to this target email !"));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Already subscribed to this target email !\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/subscribe\"}"
                , content);
    }

    @Test
    public void blockEmailSuccessTest() throws Exception {
        //Test with success case
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("nguyenphi@gmail.com");

        when(relationService.blockEmail(any(SubscribeAndBlockRequest.class))).thenReturn(true);

        String json = objectMapper.writeValueAsString(subBlockRequest);
        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true}", content);
    }

    @Test
    public void blockEmailInvalidInputTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester(null);
        subBlockRequest.setTarget("target@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);
        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"Requester email must not be empty or null\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}", content);

    }

    @Test
    public void blockEmailInvalidEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("requesterGmail.com");
        subBlockRequest.setTarget("target@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Invalid input email format\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/block\"}"
                , content);
    }

    @Test
    public void blockEmailSameEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("hongson@gmail.com");

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"Input emails duplicated\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/block\"}"
                , content);
    }

    @Test
    public void blockEmailNotExistEmailTest() throws Exception {
        //Test with invalid input
        subBlockRequest.setRequester("xxyyzz@gmail.com");
        subBlockRequest.setTarget("target@gmail.com");

        when(relationService.blockEmail(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":404,\"message\":\"Input email not found\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/block\"}"
                , content);
    }

    @Test
    public void blockEmailBlockedEmailTest() throws Exception {
        //Test with blocked email
        subBlockRequest.setRequester("hongson@gmail.com");
        subBlockRequest.setTarget("nguyenquang@gmail.com");

        when(relationService.blockEmail(any(SubscribeAndBlockRequest.class)))
                .thenThrow(new StatusException("This email has already being blocked !"));

        String json = objectMapper.writeValueAsString(subBlockRequest);

        MvcResult result= mockMvc.perform(put("/api/emails/block")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":400,\"message\":\"This email has already being blocked !\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/block\"}"
                , content);
    }

    @Test
    public void retrieveEmailSuccessTest() throws Exception {
        //Test with success case
        Set<String> setEmails1 = new HashSet<>();
        setEmails1.add("saomai@gmail.com");
        setEmails1.add("minhthong@gmail.com");
        retrieveRequest = new RetrieveRequest("hongson@gmail.com", "Hello phuongthao@gmail.com");

        when(relationService.retrieveEmails(any(RetrieveRequest.class)))
                .thenReturn(setEmails1);

        String json = objectMapper.writeValueAsString(retrieveRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/retrieve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"success\":true,\"recipients\":[\"saomai@gmail.com\",\"minhthong@gmail.com\"]}", content);
    }

    @Test
    public void retrieveEmailInvalidTest() throws Exception {
        //Test with invalid RetrieveRequest
        retrieveRequest = new RetrieveRequest(null, "Hello haong@gmail.com");

        String json = objectMapper.writeValueAsString(retrieveRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/retrieve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"error\":[\"Sender email must not be null or empty\"],\"timestamp\":\""+ timestamp +"\",\"status\":400}", content);

    }

    @Test
    public void retrieveNotExistEmailTest() throws Exception {
        //Test with invalid RetrieveRequest
        retrieveRequest = new RetrieveRequest("vuiquanghu@gmail.com", "Hello haong@gmail.com");

        when(relationService.retrieveEmails(any(RetrieveRequest.class)))
                .thenThrow(new DataNotFoundException(ErrorConstraints.EMAIL_NOT_FOUND));

        String json = objectMapper.writeValueAsString(retrieveRequest);

        MvcResult result= mockMvc.perform(post("/api/emails/retrieve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        assertEquals("{\"statusCode\":404,\"message\":\"Input email not found\",\"timestamp\":\""+ timestamp +"\",\"description\":\"uri=/api/emails/retrieve\"}"
                , content);
    }

    @Test
    public void retrieveNotFoundTest() throws Exception {
        //Test with not found case
        retrieveRequest = new RetrieveRequest("alone@gmail.com", "Hello hang@gmail.com");
        setEmails = Collections.emptySet();
        when(relationService.retrieveEmails(any(RetrieveRequest.class)))
                .thenReturn(setEmails);

        String json = objectMapper.writeValueAsString(retrieveRequest);

        mockMvc.perform(post("/api/emails/retrieve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}
