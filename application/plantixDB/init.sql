CREATE DATABASE IF NOT EXISTS plantix_db;
USE plantix_db;

CREATE TABLE IF NOT EXISTS users(
    id BIGINT NOT NULL AUTO_INCREMENT,
    dni BIGINT NOT NULL,
    email VARCHAR(255),
    password VARCHAR(255),
    registration_date DATETIME(6),
    PRIMARY KEY(id)
);

INSERT INTO users(id, dni, email, password, registration_date)
VALUES(
    1,
    111111111,
    "admin@gmail.com",
    "$2a$10$Ls7gjNQ9Fnbv5vCmY0SX1OXMMdQm4r0nwd6aAhXFkIRTVs/RviwBO",
    NOW()
);

CREATE TABLE IF NOT EXISTS roles(
    id BIGINT NOT NULL AUTO_INCREMENT,
    role VARCHAR(255),
    PRIMARY KEY(id)
);

INSERT INTO roles(id, role) VALUES(1, "DEVELOPER");
INSERT INTO roles(id, role) VALUES(2, "PROFESOR");
INSERT INTO roles(id, role) VALUES(3, "ESTUDIANTE");

CREATE TABLE IF NOT EXISTS user_role(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY(user_id, role_id)
);

INSERT INTO user_role(user_id, role_id) VALUES(1, 1);