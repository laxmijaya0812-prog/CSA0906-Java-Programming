CREATE DATABASE IF NOT EXISTS transport_db;
USE transport_db;

CREATE TABLE IF NOT EXISTS bus_pass (
    pass_id VARCHAR(20) PRIMARY KEY,
    reg_no VARCHAR(20) NOT NULL,
    name VARCHAR(100) NOT NULL,
    route_no INT NOT NULL,
    amount DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS student (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    register_no VARCHAR(30),
    department VARCHAR(100)
);
