package com.s3.friendsmanagement.utils;

import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.RetrieveRequest;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;
public class UtilsTest {
    @Data
    @Builder
    static class ExpectData {
        protected String name;
        protected String email;
        protected Boolean isTrue;
        protected Object request;
        protected String error;
    }

    @Test
    public void checkCreateFriendConnectionReqSuccess() {
        List<String> friends = new ArrayList<>();
        friends.add("test@gmail.com");
        friends.add("son@gmail.com");
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(friends);
        Assertions.assertEquals("", RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkCreateFriendConnectionReqNullList() {
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(null);
        Assertions.assertEquals(ErrorConstraints.INVALID_REQUEST, RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkCreateFriendConnectionReqInvalid() {
        List<String> friends = new ArrayList<>();
        friends.add("testGmail.com");
        friends.add(null);
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(friends);
        Assertions.assertEquals(ErrorConstraints.INVALID_REQUEST, RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkCreateFriendConnectionReqLackEmail() {
        List<String> friends = new ArrayList<>();
        friends.add("test@gmail.com");
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(friends);
        Assertions.assertEquals(ErrorConstraints.INVALID_SIZE, RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkCreateFriendConnectionReqInvalidEmail() {
        List<String> friends = new ArrayList<>();
        friends.add("testGmail.com");
        friends.add("son@gmail.com");
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(friends);
        Assertions.assertEquals(ErrorConstraints.INVALID_EMAIL, RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkCreateFriendConnectionReqDuplicateEmail() {
        List<String> friends = new ArrayList<>();
        friends.add("test@gmail.com");
        friends.add("test@gmail.com");
        CreateFriendConnectionReq request = new CreateFriendConnectionReq(friends);
        Assertions.assertEquals(ErrorConstraints.EMAIL_DUPLICATED, RequestValidation.checkCreateFriendConnectionReq(request));
    }
    @Test
    public void checkSubscribeAndBlockRequestSuccess() {
        SubscribeAndBlockRequest request = new SubscribeAndBlockRequest("test@gmail.com", "son@gmail.com");
        Assertions.assertEquals("", RequestValidation.checkSubscribeAndBlockRequest(request));
    }
    @Test
    public void checkSubscribeAndBlockRequestInvalid() {
        SubscribeAndBlockRequest request = new SubscribeAndBlockRequest(null, "son@gmail.com");
        Assertions.assertEquals(ErrorConstraints.INVALID_REQUEST, RequestValidation.checkSubscribeAndBlockRequest(request));
    }
    @Test
    public void checkSubscribeAndBlockRequestInvalidEmail() {
        SubscribeAndBlockRequest request = new SubscribeAndBlockRequest("sonGmail.com", "son@gmail.com");
        Assertions.assertEquals(ErrorConstraints.INVALID_EMAIL, RequestValidation.checkSubscribeAndBlockRequest(request));
    }
    @Test
    public void checkSubscribeAndBlockRequestDuplicateEmail() {
        SubscribeAndBlockRequest request = new SubscribeAndBlockRequest("son@gmail.com", "son@gmail.com");
        Assertions.assertEquals(ErrorConstraints.EMAIL_DUPLICATED, RequestValidation.checkSubscribeAndBlockRequest(request));
    }
    @Test
    public void checkRetrieveRequestSuccess() {
        RetrieveRequest request = new RetrieveRequest("test@gmail.com", "son@gmail.com");
        Assertions.assertEquals("", RequestValidation.checkRetrieveRequest(request));
    }
    @Test
    public void checkRetrieveRequestInvalid() {
        RetrieveRequest request = new RetrieveRequest(null, "hongson@gmail.com");
        Assertions.assertEquals(ErrorConstraints.INVALID_REQUEST, RequestValidation.checkRetrieveRequest(request));
    }
    @Test
    public void checkRetrieveRequestInvalidEmail() {
        RetrieveRequest request = new RetrieveRequest("sonGmail.com", "son@gmail.com");
        Assertions.assertEquals(ErrorConstraints.INVALID_EMAIL, RequestValidation.checkRetrieveRequest(request));
    }
    @Test
    public void getEmailsFromTextTest() {
        String text = "test@gmail.com   hongson@gmail.com";
        Set<String> expect = new HashSet<>();
        expect.add("test@gmail.com");
        expect.add("hongson@gmail.com");
        Set<String> actual = EmailUtils.getEmailsFromText(text);
        Assertions.assertEquals(expect, actual);
    }
    @Test
    public void isEmailTest() {
        List<ExpectData> tcs = new ArrayList<>();
        tcs.addAll(Arrays.asList(new ExpectData.ExpectDataBuilder()
                        .isTrue(true)
                        .email("test@gmail.com")
                        .build(),
                new ExpectData.ExpectDataBuilder()
                        .isTrue(false)
                        .email("test.com")
                        .build())
        );
        for (ExpectData e: tcs) {
            Assertions.assertEquals(e.isTrue, EmailUtils.isEmail(e.email));
        }
    }
}