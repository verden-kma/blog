INSERT INTO user_role (role_id, role) VALUES
(1, 'PUBLISHER'),
(2, 'ADMIN');

INSERT INTO user_permission (permission_id, permission) VALUES
(1, 'POST_RECORDS'),
(2, 'POST_COMMENTS'),
(3, 'EVALUATE'),
(4, 'FOLLOW'),
(5, 'ADD_ADMINS'),
(6, 'DELETE_OTHER_RECORD'),
(7, 'DELETE_OTHER_COMMENT'),
(8, 'BAN_OTHERS');

INSERT INTO role_to_permission (role_id, permission_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(2, 1),
(2, 2),
(2, 5),
(2, 6),
(2, 7),
(2, 8);

-- password = 'adminpass'
INSERT INTO user_entity (id, username, email, encrypted_password, role_id, is_active, status, description) VALUES
(1, 'admin', 'admin@sprout.com', '$2a$10$gTb5.07BiacADrxy5Cg0pO8.0AKwmbgLzlRs6IMwHFZ2JyqpBmfQO', 2, true, 'default admin', 'meant to demonstrate admin functionality');

INSERT INTO publisher_stats (publisher_id, uploads, followers, likes, dislikes, comments) VALUES
(1, 0, 0, 0, 0, 0);

-- password = '00000'
-- there is a non-auto id generator for UserEntity which depends on the number of hardcoded users
insert into user_entity (id, username, email, encrypted_password, role_id, is_active, status, description) values
(2, 'niceUsername', 'mock1@mail.com', '$2a$10$WWZ6p..W7MMR8UcJKlUEO.VQeoVfE2HNyFhiKeHHHHZAeDmbhlRxO', 1, true, 'someStatus', 'someDesc');
--(2, 'Orest', 'mock2@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'OrestStatus', 'OrestDesc'),
--(3, 'Vasyl', 'mock3@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'VasylStatus', 'VasylDesc'),
--(4, 'Yaroslav', 'mock4@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'YaroslavStatus', 'YaroslavDesc'),
--(5, 'Mykhailo', 'mock5@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'MykhailoStatus', 'MykhailoDesc'),
--(6, 'Ivan', 'mock6@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'IvanStatus', 'IvanDesc'),
--(7, 'Kyrylo', 'mock7@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'KyryloStatus', 'KyryloDesc'),
--(8, 'Volodymyr', 'mock8@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'VolodymyrStatus', 'VolodymyrDesc'),
--(9, 'Svyatoslav', 'mock9@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'SvyatoslavStatus', 'SvyatoslavDesc'),
--(10, 'Hryhir', 'mock10@mail.com', '$2a$10$Cx/wrhb468//X7a66LM8puEbfXPYE8gi6PHWzyTv9ktggx7q3i4gO', 1, true, 'HryhirStatus', 'HryhirDesc');

INSERT INTO publisher_stats (publisher_id, uploads, followers, likes, dislikes, comments) VALUES
(2, 0, 0, 0, 0, 0);