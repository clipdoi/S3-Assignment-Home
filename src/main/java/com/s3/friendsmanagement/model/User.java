package com.s3.friendsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public")
public class User implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "email", nullable = false, length = 250)
	private String email;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userByFriendId")
	private Set<UserRelationship> userRelationshipsForFriendId = new HashSet<UserRelationship>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userByEmailId")
	private Set<UserRelationship> userRelationshipsForEmailId = new HashSet<UserRelationship>(0);

	public User(long id, String email) {
		this.id = id;
		this.email = email;
	}

}
