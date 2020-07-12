create table brand (
    id serial primary key,
    name text
)

insert into brand (name) values ('Skoda'), ('Lexus'), ('BMW'), ('Lada'), ('Audi')

create table status (
    id serial primary key,
    description text
)

insert into status (description) values ('продано'), ('продаётся')

create table usr (
    id serial primary key,
    login text
)

create table transmission (
    id serial primary key,
    name text
)

insert into transmission (name) values ('автомат'), ('механика'), ('робот'), ('вариатор')

create table model (
    id serial primary key,
    description text,
    brand_id int not null references brand(id)
)

insert into model (description, brand_id) values ('Rapid', 1), ('Octavia', 1), ('ES250', 2), ('IZ300', 2),
('325', 3), ('525', 3), ('2106', 4), ('2109', 4), ('2114', 4), ('A7', 5)

create table model_transmission (
	model_id int not null references model(id),
   	transmission_id int not null references transmission(id)
)

insert into model_transmission (model_id, transmission_id) values (1, 1), (1, 2), (1, 3),
(2, 1), (2, 2), (2, 3), (3, 1), (4, 1), (5, 1), (6, 1), (7, 2), (8, 2), (9, 2), (11, 4)

create table advertisement (
    id serial primary key,
    description text,
    status_id int not null references status(id),
    user_id int not null references usr(id),
    model_id int not null references model(id),
    transmission_id int not null references transmission(id),
    owners text,
    year_issue int,
    photo text
)