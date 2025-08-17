INSERT INTO roles(name) VALUES('ROLE_ACTUATOR')
ON DUPLICATE KEY UPDATE roles.name = 'ROLE_ACTUATOR';

INSERT INTO users(username, email, password)
VALUES('cws_actuator', 'cws_actuator@mail.de', '$2a$10$Sop81gNmP8pSwXcqE3mINexL7zgAdjJEYBnc2expstzL8mUw58f0m')
ON DUPLICATE KEY UPDATE users.username = 'cws_actuator';

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id FROM users, roles
WHERE users.username='cws_actuator' AND roles.name = 'ROLE_ACTUATOR'
ON DUPLICATE KEY UPDATE user_roles.user_id = (SELECT users.id FROM users WHERE users.username = 'cws_actuator');