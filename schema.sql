CREATE DATABASE IF NOT EXISTS employee_directory;
USE employee_directory;

DROP TABLE IF EXISTS employee;
CREATE TABLE employee (
    id         INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(45) DEFAULT NULL,
    last_name  VARCHAR(45) DEFAULT NULL,
    email      VARCHAR(45) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO employee (first_name, last_name, email) VALUES
    ('Leslie', 'Andrews',    'leslie@test.com'),
    ('Emma',   'Baumgarten', 'emma@test.com'),
    ('Avani',  'Gupta',      'avani@test.com');
