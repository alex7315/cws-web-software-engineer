insert into roles(name)
values('ROLE_USER');

insert into roles(name)
values('ROLE_ADMIN');

insert into users(username, email, password)
values('user1', 'user1@mail.de', 'password1');

insert into users(username, email, password)
values('user2', 'user2@mail.de', 'password2');

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user1' and roles.name = 'ROLE_USER';

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user2' and roles.name = 'ROLE_USER';

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user2' and roles.name = 'ROLE_ADMIN';
