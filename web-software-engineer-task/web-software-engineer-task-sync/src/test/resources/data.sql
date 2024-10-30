insert into roles(name)
values('ROLE_USER');

insert into roles(name)
values('ROLE_ADMIN');

insert into users(username, email, password)
values('user', 'user@mail.de', '$2a$10$qSyi8szcIJtEtfg66B0A5.TpVztFU2dude0zj0YMZ6hIhs5SAu6Oe');

insert into users(username, email, password)
values('admin', 'admin@mail.de', '$2a$10$qSyi8szcIJtEtfg66B0A5.TpVztFU2dude0zj0YMZ6hIhs5SAu6Oe');

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user' and roles.name = 'ROLE_USER';

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='admin' and roles.name = 'ROLE_ADMIN';
