insert into movie (id, title, director, year)
values (1, 'Dune', 'Denis Villeneuve', '2021'),
       (2, 'Free guy', 'Shawn Levy', '2021'),
       (3, 'Venom', 'Ruben Fleischer', '2018');

insert into schedule (id, room_number, time, week, movie_id)
values (nextval('hibernate_sequence'), 1, '13:00:00', 0, 1),
       (nextval('hibernate_sequence'), 1, '13:00:00', 1, 1),
       (nextval('hibernate_sequence'), 1, '13:00:00', 2, 1),
       (nextval('hibernate_sequence'), 1, '13:00:00', 3, 1),
       (nextval('hibernate_sequence'), 1, '13:00:00', 4, 1),
       (nextval('hibernate_sequence'), 2, '18:00:00', 0, 2),
       (nextval('hibernate_sequence'), 2, '18:00:00', 1, 2),
       (nextval('hibernate_sequence'), 2, '18:00:00', 2, 2),
       (nextval('hibernate_sequence'), 2, '18:00:00', 3, 2),
       (nextval('hibernate_sequence'), 2, '18:00:00', 4, 2),
       (nextval('hibernate_sequence'), 1, '16:00:00', 5, 3),
       (nextval('hibernate_sequence'), 1, '16:00:00', 6, 3);