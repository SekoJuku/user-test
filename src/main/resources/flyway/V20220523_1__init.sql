drop table if exists countries;
drop table if exists users;
drop table if exists roles;
drop table if exists images;

create table countries
(
    id   bigint generated by default as identity
        constraint countries_pkey
            primary key,
    name varchar(255),
    CONSTRAINT countries_uk UNIQUE (name)
);

alter table countries
    owner to postgres;

create table images
(
    id           bigint generated by default as identity
        constraint images_pkey
            primary key,
    content_type varchar(255),
    data         bytea,
    extension    varchar(255),
    name         varchar(255)
);

alter table images
    owner to postgres;

create table roles
(
    id   bigint generated by default as identity
        constraint roles_pkey
            primary key,
    name varchar(255)
);

alter table roles
    owner to postgres;

create table users
(
    id           bigint generated by default as identity
        constraint users_pkey
            primary key,
    email        varchar(255),
    middlename   varchar(255),
    name         varchar(255),
    password     varchar(255),
    phone_number varchar(255),
    sex          varchar(255),
    surname      varchar(255),
    country_id   bigint,
    image_id     bigint,
    role_id      bigint,
    FOREIGN KEY (country_id) REFERENCES countries,
    FOREIGN KEY (image_id) REFERENCES images,
    FOREIGN KEY (role_id) REFERENCES roles
);

alter table users
    owner to postgres;