package org.cws.web.software.engineer.task.persistence.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "github_user")
public class GithubUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "github_id", nullable = false)
	private Long githubId;

	@Column(name = "login", nullable = false)
	private String login;

	@Column(name = "modified_at", nullable = false)
	private Instant modifiedAt;

}
