package com.s3.friendsmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFriendConnectionReq {

    @NotNull(message = "List email must not be null or empty")
    private List<String> friends;
}
