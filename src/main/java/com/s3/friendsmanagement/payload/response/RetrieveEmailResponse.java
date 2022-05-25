package com.s3.friendsmanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveEmailResponse {
    private Boolean success;
    private Set<String> recipients;

}
