-- TODO: config for mysql database when deploy
MERGE INTO t_permissions (id, service_name) KEY(id) VALUES (0, 'basic');
MERGE INTO t_permissions (id, service_name) KEY(id) VALUES (1, 'hadoop-client');
MERGE INTO t_roles (id, role_name, permission_level, description) KEY(id) VALUES (1, 'admin', 9999, '');
MERGE INTO t_roles (id, role_name, permission_level, description) KEY(id) VALUES (2, 'editor', 999, '');
MERGE INTO t_roles (id, role_name, permission_level, description) KEY(id) VALUES (3, 'vip user', 99,'user pay for vip service');
MERGE INTO t_roles (id, role_name, permission_level, description) KEY(id) VALUES (4, 'user', 9, 'basic user');
MERGE INTO t_roles (id, role_name, permission_level, description) KEY(id) VALUES (5, 'guest', 0, 'user without login');
