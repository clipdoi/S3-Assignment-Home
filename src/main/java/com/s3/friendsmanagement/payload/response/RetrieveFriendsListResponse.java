package com.s3.friendsmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveFriendsListResponse {
    private Boolean success;
    private List<String> friends;
    private int count;

}
