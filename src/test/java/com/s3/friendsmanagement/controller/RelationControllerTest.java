package com.s3.friendsmanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.EmailRequest;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.repository.UserRelationshipRepository;
import com.s3.friendsmanagement.repository.UserRepository;
import com.s3.friendsmanagement.service.RelationService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        listEmail.add("quan@gmail.com");
        listEmail.add("hoang@gmail.com");

        setEmails = new HashSet<>();
        setEmails.add("quan@gmail.com");
        setEmails.add("hoang@gmail.com");

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
}
