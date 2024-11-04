insert into github_user(github_id, login, modification_id)
values(1, 'r-test-user-1', 1);

insert into github_user(github_id, login, modification_id)
values(2, 'n-test-user-2', 2);

insert into github_user(github_id, login, modification_id)
values(3, 'c-test-user-3', 3);

insert into github_user(github_id, login, modification_id)
values(4, 'x-test-user-4', 4);

insert into github_user(github_id, login, modification_id)
values(5, 'f-test-user-5', 5);

insert into github_user(github_id, login, modification_id)
values(6, 'a-test-user-6', 6);

insert into github_user(github_id, login, modification_id)
values(7, 'l-test-user-7', 7);

insert into github_user(github_id, login, modification_id)
values(8, 'h-test-user-8', 8);

insert into github_user(github_id, login, modification_id)
values(9, 'u-test-user-9', 9);

insert into github_user(github_id, login, modification_id)
values(10, 'w-test-user-10', 10);

insert into roles(name) values('ROLE_USER');
insert into roles(name) values('ROLE_ADMIN');

insert into users(username, email, password)
values('user1', 'user1@mail.de', '$2a$10$Sop81gNmP8pSwXcqE3mINexL7zgAdjJEYBnc2expstzL8mUw58f0m');

insert into users(username, email, password)
values('user2', 'user2@mail.de', '$2a$10$Sop81gNmP8pSwXcqE3mINexL7zgAdjJEYBnc2expstzL8mUw58f0m');

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user1' and roles.name = 'ROLE_USER';

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user2' and roles.name = 'ROLE_USER';

insert into user_roles(user_id, role_id)
select users.id, roles.id from users, roles
where users.username='user2' and roles.name = 'ROLE_ADMIN';