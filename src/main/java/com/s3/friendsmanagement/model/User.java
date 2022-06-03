package com.s3.friendsmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public",
		uniqueConstraints = @UniqueConstraint(columnNames = { "username", "email" }))
public class User implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@Column(name = "email", nullable = false, length = 250)
	private String email;

	@NotBlank
	@Size(max = 250)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

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

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
