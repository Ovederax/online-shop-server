DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE `onlineshop`;
USE `onlineshop`;

CREATE TABLE users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,

    UNIQUE KEY users(login),
    PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE administrators (
    user_id INT(11) NOT NULL AUTO_INCREMENT,
    position VARCHAR(50) NOT NULL,

	FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY(user_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE clients (
    user_id INT(11) NOT NULL AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    address VARCHAR(50) NOT NULL,
    phone VARCHAR(50) NOT NULL,

	FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY(user_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE goods (
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

CREATE TABLE goods_categories(
    id INT(11) NOT NULL AUTO_INCREMENT,
    goodsId INT(11) NOT NULL,
    categoryId INT(11) NOT NULL,

    UNIQUE KEY goods_categories(goodsId, categoryId),
    FOREIGN KEY(goodsId) REFERENCES goods(id),
    FOREIGN KEY(categoryId) REFERENCES categories(id),
    PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE logged_users (
    user_id INT(11) NOT NULL AUTO_INCREMENT,
    token VARCHAR(50) NOT NULL,

	FOREIGN KEY (user_id) REFERENCES users(id),
    PRIMARY KEY(user_id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE baskets (
    id INT(11) NOT NULL AUTO_INCREMENT,
    userId INT(11) NOT NULL,
    goodsId INT(11) NOT NULL,
    count INT(11),

    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY(goodsId) REFERENCES goods(id),
    UNIQUE KEY goods_categories(userId, goodsId)
) ENGINE=INNODB DEFAULT CHARSET=utf8;