CREATE TABLE IF NOT EXISTS refresh_token (
	id bigint NOT NULL AUTO_INCREMENT,
	user_id bigint NOT NULL,
	token varchar(100) NOT NULL,
	expiry_date timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (token),
	KEY fk1refresh_token_user (user_id),
	CONSTRAINT fk1refresh_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT='Refresh token table';