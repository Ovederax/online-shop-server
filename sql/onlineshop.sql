DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE `onlineshop`;
USE `onlineshop`;

CREATE TABLE users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login VARCHAR(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    password VARCHAR(50) NOT NULL,

    UNIQUE KEY users(login),
    PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE administrators (
    userId INT(11) NOT NULL,
    position VARCHAR(50) NOT NULL,

	FOREIGN KEY (userId) REFERENCES users(id),
    PRIMARY KEY(userId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE clients (
    userId INT(11) NOT NULL,
    email VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,

	FOREIGN KEY (userId) REFERENCES users(id),
    PRIMARY KEY(userId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE products (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    price INT(11),
    counter INT(11),
	isDeleted TINYINT (1),

    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE categories (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    parentId INT(11),

    PRIMARY KEY (id),
    FOREIGN KEY (parentId) REFERENCES categories(id) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE products_categories(
    id INT(11) NOT NULL AUTO_INCREMENT,
    productId INT(11) NOT NULL,
    categoryId INT(11) NOT NULL,

    UNIQUE KEY products_categories(productId, categoryId),
    FOREIGN KEY(productId)  REFERENCES products(id),
    FOREIGN KEY(categoryId) REFERENCES categories(id),
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE logged_users (
    userId INT(11) NOT NULL AUTO_INCREMENT,
    token VARCHAR(50) NOT NULL,

	FOREIGN KEY (userId) REFERENCES users(id),
    PRIMARY KEY(userId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE baskets (
    id INT(11) NOT NULL AUTO_INCREMENT,
    userId INT(11) NOT NULL,
    productId INT(11) NOT NULL,
    count INT(11),

    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY(productId) REFERENCES products(id),
    UNIQUE KEY products_categories(userId, productId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE purchases (
    id INT(11) NOT NULL AUTO_INCREMENT,
    actualId INT(11) NOT NULL,
    clientId INT(11) NOT NULL,
    name VARCHAR(50) NOT NULL,
    buyCount INT(11) NOT NULL,
    buyPrice INT(11) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY(actualId) REFERENCES products(id),
    FOREIGN KEY(clientId) REFERENCES clients(userId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE deposits (
    id INT(11) NOT NULL AUTO_INCREMENT,
    clientId  INT(11) NOT NULL,
    money INT(11) NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (clientId) REFERENCES clients(userId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;