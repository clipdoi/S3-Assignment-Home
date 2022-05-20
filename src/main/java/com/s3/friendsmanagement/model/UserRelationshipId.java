package com.s3.friendsmanagement.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRelationshipId implements java.io.Serializable {

	@Column(name = "email_id", nullable = false)
	private long emailId;

	@Column(name = "friend_id", nullable = false)
	private long friendId;

	@Column(name = "status", nullable = false, length = 50)
	private String status;

}
