drop table customers if exists;
create table customers
(
    id   int not null auto_increment,
    name varchar(255),
    constraint pk primary key (id)
);