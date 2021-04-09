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

--insert into user_entity (id, username, encrypted_password, status, description) values
--(1, 'niceUsername', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'someStatus', 'someDesc'),
--(2, 'Orest', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'OrestStatus', 'OrestDesc'),
--(3, 'Vasyl', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'VasylStatus', 'VasylDesc'),
--(4, 'Yaroslav', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'YaroslavStatus', 'YaroslavDesc'),
--(5, 'Mykhailo', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'MykhailoStatus', 'MykhailoDesc'),
--(6, 'Ivan', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'IvanStatus', 'IvanDesc'),
--(7, 'Kyrylo', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'KyryloStatus', 'KyryloDesc'),
--(8, 'Volodymyr', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'VolodymyrStatus', 'VolodymyrDesc'),
--(9, 'Svyatoslav', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'SvyatoslavStatus', 'SvyatoslavDesc'),
--(10, 'Hryhir', '$2a$10$aaroqu8EsZpaS4S8lvtKcu73lO6ltyVU71IqBTE3hj0PrOL7BAEDK', 'HryhirStatus', 'HryhirDesc');