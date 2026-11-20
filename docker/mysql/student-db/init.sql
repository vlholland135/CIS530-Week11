-- Runs once, when the student-db volume is first created.
-- MYSQL_DATABASE in docker-compose.yml creates student_db; this line keeps the script safe to run by hand too.
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO students (first_name, last_name, email) VALUES
('Ali', 'Rezaei', 'ali.rezaei@example.com'),
('Sara', 'Johnson', 'sara.johnson@example.com'),
('David', 'Miller', 'david.miller@example.com');
