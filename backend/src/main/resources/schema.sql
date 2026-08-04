CREATE DATABASE IF NOT EXISTS portfolio_db;

USE portfolio_db;


CREATE TABLE IF NOT EXISTS market (

    id INT AUTO_INCREMENT PRIMARY KEY,

    symbol VARCHAR(10) NOT NULL UNIQUE,

    company_name VARCHAR(100) NOT NULL,

    exchange VARCHAR(50),

    sector VARCHAR(50),

    current_price DECIMAL(10,2),

    change_percent DECIMAL(6,2) DEFAULT 0

);


CREATE TABLE IF NOT EXISTS holding (

    id INT AUTO_INCREMENT PRIMARY KEY,

    market_id INT NOT NULL,

    quantity INT NOT NULL,

    purchase_price DECIMAL(10,2),

    purchase_date DATE,

    CONSTRAINT fk_holding_market

    FOREIGN KEY (market_id)

    REFERENCES market(id)

);