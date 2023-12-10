INSERT INTO t_permissions (id, service_name, permission_bit) VALUES (0, 'basic', 1);
INSERT INTO t_permissions (id, service_name, permission_bit) VALUES (1, 'hadoop-client', 1);
INSERT INTO t_roles (id, role_name, permission_level, description) VALUES (1, 'admin', 9999, '');
INSERT INTO t_roles (id, role_name, permission_level, description) VALUES (2, 'editor', 999, '');
INSERT INTO t_roles (id, role_name, permission_level, description) VALUES (3, 'vip user', 99,'user pay for vip service');
INSERT INTO t_roles (id, role_name, permission_level, description) VALUES (4, 'user', 9, 'basic user');
INSERT INTO t_roles (id, role_name, permission_level, description) VALUES (5, 'guest', 0, 'user without login');