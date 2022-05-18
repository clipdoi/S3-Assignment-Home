package com.s3.friendsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_relationship", schema = "public")
public class UserRelationship implements java.io.Serializable {

	@EmbeddedId
	@AttributeOverrides({ @AttributeOverride(name = "emailId", column = @Column(name = "email_id", nullable = false)),
			@AttributeOverride(name = "friendId", column = @Column(name = "friend_id", nullable = false)),
			@AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 50)) })
	private UserRelationshipId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "friend_id", nullable = false, insertable = false, updatable = false)
	private User userByFriendId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email_id", nullable = false, insertable = false, updatable = false)
	private User userByEmailId;



}
