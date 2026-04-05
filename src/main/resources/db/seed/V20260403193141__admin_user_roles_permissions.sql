INSERT INTO public.permissions ("name","type") VALUES
	 ('user_create','SYSTEM'::public."PermissionType"),
	 ('user_read','SYSTEM'::public."PermissionType"),
	 ('user_update_self','SYSTEM'::public."PermissionType"),
	 ('user_delete_self','SYSTEM'::public."PermissionType"),
	 ('board_create','SYSTEM'::public."PermissionType"),
	 ('board_delete','BOARD'::public."PermissionType"),
	 ('board_read','SYSTEM'::public."PermissionType"),
	 ('board_update','BOARD'::public."PermissionType"),
	 ('board_restore','SYSTEM'::public."PermissionType"),
	 ('board_read_full_board','BOARD'::public."PermissionType"),
	 ('board_add_members','BOARD'::public."PermissionType"),
	 ('board_remove_members','BOARD'::public."PermissionType"),
	 ('board_update_member_role','BOARD'::public."PermissionType"),
	 ('board_view_members','BOARD'::public."PermissionType"),
	 ('list_create','BOARD'::public."PermissionType"),
	 ('list_read','BOARD'::public."PermissionType"),
	 ('list_update','BOARD'::public."PermissionType"),
	 ('list_delete','BOARD'::public."PermissionType"),
	 ('card_create','BOARD'::public."PermissionType"),
	 ('card_read','BOARD'::public."PermissionType"),
	 ('card_update','BOARD'::public."PermissionType"),
	 ('card_delete','BOARD'::public."PermissionType"),
	 ('user_update_any','SYSTEM'::public."PermissionType"),
	 ('user_delete_any','SYSTEM'::public."PermissionType"),
	 ('user_restore','SYSTEM'::public."PermissionType");

INSERT INTO public.system_roles ("name") VALUES
	 ('admin'),
	 ('user');

INSERT INTO public.board_roles ("name") VALUES
	 ('admin'),
	 ('member');

INSERT INTO public.board_role_board_permission (board_role_id,permission_id) VALUES
	 (1,6),
	 (1,8),
	 (1,10),
	 (1,11),
	 (1,12),
	 (1,13),
	 (1,14),
	 (1,15),
	 (1,16),
	 (1,17),
	 (1,18),
	 (1,19),
	 (1,20),
	 (1,21),
	 (1,22),
	 (2,10),
	 (2,14),
	 (2,16),
	 (2,19),
	 (2,20),
	 (2,21),
	 (2,22);

INSERT INTO public.system_role_system_permission (system_role_id,permission_id) VALUES
	 (1,1),
	 (1,2),
	 (1,3),
	 (1,4),
	 (1,5),
	 (1,7),
	 (1,9),
	 (1,23),
	 (1,24),
	 (1,25),
	 (2,2),
	 (2,3),
	 (2,4),
	 (2,5),
	 (2,7),
	 (2,9);
   
INSERT INTO public.profiles ("name",avatar,created_at,updated_at) VALUES
	 ('Admin',NULL,'2026-03-17 16:31:47.912','2026-03-17 16:31:47.912');
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 16:31:47.928','2026-03-17 16:31:47.928'),
	--  ('Jane Nikolaus','https://img.freepik.com/premium-vector/smiling-woman-avatar_937492-6135.jpg','2026-03-17 16:31:47.938','2026-03-17 16:31:47.938'),
	--  ('Mari Doe','https://img.freepik.com/vector-premium/retrato-mujer-negocios_505024-2787.jpg','2026-03-17 16:31:47.947','2026-03-17 16:31:47.947'),
	--  ('Jeff Beli','https://www.pngarts.com/files/5/User-Avatar-PNG-Transparent-Image.png','2026-03-17 16:31:47.959','2026-03-17 16:31:47.959'),
	--  ('Admin',NULL,'2026-03-17 16:59:38.783','2026-03-17 16:59:38.783'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 16:59:38.81','2026-03-17 16:59:38.81'),
	--  ('Admin',NULL,'2026-03-17 16:59:44.694','2026-03-17 16:59:44.694'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 16:59:44.724','2026-03-17 16:59:44.724'),
	--  ('Admin',NULL,'2026-03-17 16:59:50.878','2026-03-17 16:59:50.878'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 16:59:50.923','2026-03-17 16:59:50.923'),
	--  ('Admin',NULL,'2026-03-17 16:59:58.087','2026-03-17 16:59:58.087'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 16:59:58.125','2026-03-17 16:59:58.125'),
	--  ('Admin',NULL,'2026-03-17 17:00:04.584','2026-03-17 17:00:04.584'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:04.623','2026-03-17 17:00:04.623'),
	--  ('Admin',NULL,'2026-03-17 17:00:11.303','2026-03-17 17:00:11.303'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:11.338','2026-03-17 17:00:11.338'),
	--  ('Admin',NULL,'2026-03-17 17:00:18.365','2026-03-17 17:00:18.365'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:18.399','2026-03-17 17:00:18.399'),
	--  ('Admin',NULL,'2026-03-17 17:00:24.872','2026-03-17 17:00:24.872'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:24.905','2026-03-17 17:00:24.905'),
	--  ('Admin',NULL,'2026-03-17 17:00:31.704','2026-03-17 17:00:31.704'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:31.743','2026-03-17 17:00:31.743'),
	--  ('Admin',NULL,'2026-03-17 17:00:38.238','2026-03-17 17:00:38.238'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:38.286','2026-03-17 17:00:38.286'),
	--  ('Admin',NULL,'2026-03-17 17:00:44.982','2026-03-17 17:00:44.982'),
	--  ('John Daniel','https://img.freepik.com/free-vector/smiling-young-man-illustration_1308-174669.jpg','2026-03-17 17:00:45.02','2026-03-17 17:00:45.02');

INSERT INTO public.users (email,"password",profile_id,system_role_id,created_at,updated_at,deleted_at) VALUES
	 ('admin@boards.com','$2b$10$Eym48W8JIdwlbpN4Q/a6N.7fSaigBZ7rpxQsSYN5X8uiGPhcPs7y2',1,1,'2026-03-17 16:31:47.921','2026-03-17 16:31:47.921',FALSE);
	--  ('john.daniel@mail.com','$2b$10$KHNywjOQ/our6pDpAsYiT.nmPRO1avFQ5s/dOmKACqkGw4ZD3B/2S',2,2,'2026-03-17 16:31:47.934','2026-03-17 16:31:47.934',FALSE),
	--  ('jane.nikolaus@mail.com','$2b$10$KHNywjOQ/our6pDpAsYiT.nmPRO1avFQ5s/dOmKACqkGw4ZD3B/2S',3,2,'2026-03-17 16:31:47.941','2026-03-17 16:31:47.941',FALSE),
	--  ('mari.doe@mail.com','$2b$10$KHNywjOQ/our6pDpAsYiT.nmPRO1avFQ5s/dOmKACqkGw4ZD3B/2S',4,2,'2026-03-17 16:31:47.951','2026-03-17 16:31:47.951',FALSE),
	--  ('jeff.belli@mail.com','$2b$10$KHNywjOQ/our6pDpAsYiT.nmPRO1avFQ5s/dOmKACqkGw4ZD3B/2S',5,2,'2026-03-17 16:31:47.962','2026-03-17 16:31:47.962',FALSE);