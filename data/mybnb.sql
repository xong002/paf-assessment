create database mybnb;

use mybnb;

create table acc_occupancy(
	acc_id varchar(10) not null,
    vacancy int not null,
    primary key(acc_id)
);

create table reservations (
	resv_id varchar(8) not null,
    name varchar(128),
    email varchar(128),
    acc_id varchar(10) not null,
    arrival_date date,
    duration int,
    primary key(resv_id),
    foreign key(acc_id) references acc_occupancy(acc_id)
);