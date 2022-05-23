drop table if exists countries;
CREATE TABLE "countries" (
                             "id" serial NOT NULL,
                             "name" varchar(255) NOT NULL,
                             CONSTRAINT "countries_pk" PRIMARY KEY ("id")
);
drop table if exists "images";
CREATE TABLE "images" (
                          "id" serial NOT NULL,
                          "name" varchar(255) NOT NULL,
                          "extension" varchar(255) NOT NULL,
                          "contentType" varchar(255) NOT NULL,
                          "data" bytea NOT NULL,
                          "user_id" serial NOT NULL,
                          CONSTRAINT "images_pk" PRIMARY KEY ("id")
);
drop table if exists users;
CREATE TABLE "users" (
        "id" serial NOT NULL,
        "name" varchar(255) NOT NULL,
        "surname" varchar(255) NOT NULL,
        "middlename" varchar(255) NOT NULL,
        "sex" varchar(20) NOT NULL,
        "phone_number" varchar(20) NOT NULL,
        "email" varchar(50) NOT NULL,
        "country_id" serial NOT NULL,
        password varchar (255) NOT NULL,
        CONSTRAINT "users_pk" PRIMARY KEY ("id"),
        CONSTRAINT "users_countries_fk" FOREIGN KEY (country_id) REFERENCES countries(id)
);