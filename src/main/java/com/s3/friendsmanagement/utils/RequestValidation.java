package com.s3.friendsmanagement.utils;

import com.s3.friendsmanagement.payload.request.CreateFriendConnectionReq;
import com.s3.friendsmanagement.payload.request.SubscribeAndBlockRequest;
import com.s3.friendsmanagement.utils.ErrorConstraints;

public class RequestValidation {

    private RequestValidation(){}

    public static String checkCreateFriendConnectionReq(CreateFriendConnectionReq request) {
        if (request == null || request.getFriends() == null) {
            return ErrorConstraints.INVALID_REQUEST;
        }

        if (request.getFriends().size() != 2) {
            return ErrorConstraints.INVALID_SIZE;
        }
        if (request.getFriends().get(0) == null || request.getFriends().get(1) ==null) {
            return ErrorConstraints.INVALID_REQUEST;
        }
        if (!EmailUtils.isEmail(request.getFriends().get(0))
                || !EmailUtils.isEmail(request.getFriends().get(1))) {
            return ErrorConstraints.INVALID_EMAIL;
        }
        if (request.getFriends().get(0).equals(request.getFriends().get(1))) {
            return ErrorConstraints.EMAIL_DUPLICATED;
        }
        return "";
    }

    public static String checkSubscribeAndBlockRequest(SubscribeAndBlockRequest request) {
        if (request == null || request.getRequester() == null || request.getTarget() == null) {
            return ErrorConstraints.INVALID_REQUEST;
        }
        if (!EmailUtils.isEmail(request.getRequester())
                || !EmailUtils.isEmail(request.getTarget())) {
            return ErrorConstraints.INVALID_EMAIL;
        }
        if (request.getRequester().equals(request.getTarget())) {
            return ErrorConstraints.EMAIL_DUPLICATED;
        }
        return "";
    }

}
