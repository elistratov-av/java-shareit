INSERT INTO users (email, name)
VALUES
	('email1@yandex.ru', 'name1'),
	('email2@yandex.ru', 'name2'),
	('email3@yandex.ru', 'name3');

INSERT INTO requests (description, requestor_id, created)
VALUES
	('description1', 1, '2024-10-26 18:30:05'),
	('description2', 2, '2024-10-26 18:30:05');

INSERT INTO items (name, description, is_available, owner_id, request_id)
VALUES
	('name1', 'description1', 1, 1, null),
	('name2', 'description2', 1, 1, null),
	('name3', 'description3', 0, 1, 1);

INSERT INTO comments (text, item_id, author_id, created)
VALUES
	('text1', 1, 1, '2024-10-26 18:30:05'),
	('text2', 1, 1, '2024-10-26 18:30:05'),
	('text3', 1, 1, '2024-10-26 18:30:05');

INSERT INTO bookings (start_date, end_date, status, item_id, booker_id)
VALUES
	('2024-10-26 18:30:05', '2024-10-27 18:30:05', 'WAITING', 1, 1),
	('2024-10-26 18:30:05', '2024-11-27 18:30:05', 'WAITING', 1, 2),
	('2024-11-15 18:30:05', '2024-11-27 18:30:05', 'REJECTED', 1, 2);
