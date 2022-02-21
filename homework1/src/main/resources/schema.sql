--create schema if not exists ecomm;

CREATE TABLE IF NOT EXISTS customer (
              id SERIAL PRIMARY KEY NOT NULL,
              first_name VARCHAR(255) NOT NULL,
              last_name VARCHAR(255) NOT NULL
);

insert into customer(first_name,last_name) values ('irin','asd');
insert into customer(first_name,last_name) values ('joy','asd');
insert into customer(first_name,last_name) values ('wendy','asd')