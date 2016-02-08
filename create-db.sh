#!/bin/bash

mysql -u root <<END
drop database if exists test_db;

create database test_db;

create table test_db.examples (
  id          bigint       not null,
  value       boolean      not null,
  expected    varchar(5)   not null,
  primary key (id)
);

insert into test_db.examples (id, value, expected) values (1, 0, 'false');
insert into test_db.examples (id, value, expected) values (2, 1, 'true');
insert into test_db.examples (id, value, expected) values (3, 2, 'true');
insert into test_db.examples (id, value, expected) values (4, false, 'false');
insert into test_db.examples (id, value, expected) values (5, true, 'true');
END
