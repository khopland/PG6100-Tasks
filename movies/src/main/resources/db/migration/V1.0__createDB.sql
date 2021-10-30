create sequence hibernate_sequence start with 1 increment by 1;
create table movie
(
    id       varchar(255) not null,
    director varchar(255),
    title    varchar(255),
    year     varchar(255),
    primary key (id)
);
create table schedule
(
    id          bigint       not null,
    room_number integer      not null check (room_number >= 0),
    time        time         not null,
    week        integer      not null,
    movie_id    varchar(255) not null,
    primary key (id)
);
alter table schedule
    add constraint movieFk foreign key (movie_id) references movie;