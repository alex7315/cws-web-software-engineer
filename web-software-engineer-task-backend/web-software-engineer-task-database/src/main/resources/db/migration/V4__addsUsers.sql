insert into roles(name) values('ROLE_USER');
insert into roles(name) values('ROLE_ADMIN');

insert into users(username, email, password)
values('cws_user', 'cws_user@mail.de', '$2a$10$Sop81gNmP8pSwXcqE3mINexL7zgAdjJEYBnc2expstzL8mUw58f0m');

insert into users(username, email, password)
values('cws_admin', 'cws_admin@mail.de', '$2a$10$Sop81gNmP8pSwXcqE3mINexL7zgAdjJEYBnc2expstzL8mUw58f0m');

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='cws_user' and roles.name = 'ROLE_USER';


insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='cws_admin' and roles.name = 'ROLE_ADMIN';
