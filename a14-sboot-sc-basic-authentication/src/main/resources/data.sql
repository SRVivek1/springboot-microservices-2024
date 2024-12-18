insert into user_details (id, name, birth_date)
values (1001, 'Rahul', current_date());

insert into user_details (id, name, birth_date)
values (1002, 'Mahesh', current_date());

insert into user_details (id, name, birth_date)
values (1003, 'Vivek', current_date());

insert into post (id, description, user_id)
values (2001, 'I want to learn SBoot MServices', 1001);

insert into post (id, description, user_id)
values (2002, 'I want to learn Data engineering', 1001);

insert into post (id, description, user_id)
values (2003, 'I want to get Cloud certified.', 1002);

insert into post (id, description, user_id)
values (2004, 'I want to learn Gen. AI', 1002);