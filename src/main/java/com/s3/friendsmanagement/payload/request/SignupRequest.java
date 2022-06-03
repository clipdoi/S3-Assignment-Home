package com.s3.friendsmanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
  @NotBlank(message = "Username mustn't be empty or null")
  @Size(min = 3, max = 20)
  private String username;

  @NotBlank(message = "Email mustn't be empty or null")
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank(message = "Password mustn't be empty or null")
  @Size(min = 6, max = 40)
  private String password;

}
