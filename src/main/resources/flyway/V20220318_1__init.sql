drop table if exists users;
CREATE TABLE "users" (
        "id" serial NOT NULL,
        "name" varchar(255) NOT NULL,
        "surname" varchar(255) NOT NULL,
        "middlename" varchar(255) NOT NULL,
        "sex" varchar(20) NOT NULL,
        "phone_number" varchar(20) NOT NULL,
        "email" varchar(50) NOT NULL,
        "ip" varchar(100) NOT NULL,
        CONSTRAINT "users_pk" PRIMARY KEY ("id")
)