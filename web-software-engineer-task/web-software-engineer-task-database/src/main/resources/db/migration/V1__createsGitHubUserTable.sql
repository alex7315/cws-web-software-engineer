DROP TABLE IF EXISTS github_user;

CREATE TABLE github_user (
	id bigint(20) NOT NULL AUTO_INCREMENT,
	github_id bigint(20) NOT NULL,
	login varchar(100) NOT NULL,
	modified_at timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (github_id),
	UNIQUE (login)
) DEFAULT CHARSET=utf8;