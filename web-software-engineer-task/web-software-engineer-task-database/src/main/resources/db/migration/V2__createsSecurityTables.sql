CREATE TABLE IF NOT EXISTS users (
	id bigint NOT NULL AUTO_INCREMENT,
	username varchar(100) NOT NULL,
	email varchar(100) NOT NULL,
	password varchar(100) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (username),
	UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT='User table';

CREATE TABLE IF NOT EXISTS roles (
	id bigint NOT NULL AUTO_INCREMENT,
	name varchar(20) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT='Role table';

CREATE TABLE IF NOT EXISTS user_roles (
	id bigint NOT NULL AUTO_INCREMENT,
	user_id bigint NOT NULL,
	role_id bigint NOT NULL,
	PRIMARY KEY (id),
	KEY fk1user_roles (user_id),
	KEY fk2user_roles (role_id),
	CONSTRAINT fk1user_roles FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	CONSTRAINT fk2user_roles FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8mb4 COMMENT='table represents many-to-many relation users to roles';


