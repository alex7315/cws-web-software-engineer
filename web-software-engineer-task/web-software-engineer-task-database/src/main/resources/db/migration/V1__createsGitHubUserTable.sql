CREATE TABLE IF NOT EXISTS github_user (
	id bigint NOT NULL AUTO_INCREMENT,
	github_id bigint NOT NULL,
	login varchar(100) NOT NULL,
	modification_id bigint NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (github_id),
	UNIQUE (login)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT='Guthub user table';