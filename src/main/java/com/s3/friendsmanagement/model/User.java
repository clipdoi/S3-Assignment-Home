package com.s3.friendsmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User implements java.io.Serializable {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "email", nullable = false, length = 250)
	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userByFriendId")
	private Set<UserRelationship> userRelationshipsForFriendId = new HashSet<UserRelationship>(0);

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userByEmailId")
	private Set<UserRelationship> userRelationshipsForEmailId = new HashSet<UserRelationship>(0);

	public User(long id, String email) {
		this.id = id;
		this.email = email;
	}

}
