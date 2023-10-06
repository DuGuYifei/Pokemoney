-- TODO: config for mysql database when deploy
MERGE INTO t_permissions (id, service_name) KEY(id) VALUES (0, 'basic');
MERGE INTO t_roles (id, role_name, description) KEY(id) VALUES (1, 'admin', '');
MERGE INTO t_roles (id, role_name, description) KEY(id) VALUES (2, 'editor', '');
MERGE INTO t_roles (id, role_name, description) KEY(id) VALUES (3, 'user', 'basic user');
MERGE INTO t_roles (id, role_name, description) KEY(id) VALUES (4, 'guest', 'user without login');
MERGE INTO t_roles (id, role_name, description) KEY(id) VALUES (5, 'vip user', 'user pay for vip service');