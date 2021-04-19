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