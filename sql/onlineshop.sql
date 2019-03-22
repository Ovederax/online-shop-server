DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE `onlineshop`;
USE `onlineshop`;

CREATE TABLE administrators (
	id INT(11) NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  patronymic VARCHAR(50),
  position VARCHAR(50) NOT NULL,
  login VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,

  UNIQUE KEY administrators(login),
  PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE clients (
	id INT(11) NOT NULL AUTO_INCREMENT,
  firstname VARCHAR(50) NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  patronymic VARCHAR(50),
  email VARCHAR(50) NOT NULL,
  address VARCHAR(50) NOT NULL,
# REVU phone
  telefon VARCHAR(50) NOT NULL,
  login VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,

  UNIQUE KEY clients(login),
  PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE goods (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  price INT(11),
  counter INT(11),

  PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE categories (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  parentId INT(11),
# REVU FK parentId
  PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE goods_categories(
  id INT(11) NOT NULL AUTO_INCREMENT,
  goodsId INT(11) NOT NULL,
# REVU categoryId
  cathegoryId INT(11) NOT NULL,

  UNIQUE KEY goods_categories(goodsId, cathegoryId),
  FOREIGN KEY(goodsId) REFERENCES goods(id) ON DELETE CASCADE,
  FOREIGN KEY(cathegoryId) REFERENCES categories(id) ON DELETE CASCADE,
  PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

# REVU logged_users
CREATE TABLE logined_users (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  token VARCHAR(50) NOT NULL,

  UNIQUE KEY logined_users(name),
  PRIMARY KEY(id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;