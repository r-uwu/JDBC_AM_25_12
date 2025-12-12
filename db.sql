
DROP DATABASE IF EXISTS `JDBC_AM_25_12`;
CREATE DATABASE `JDBC_AM_25_12`;
USE `JDBC_AM_25_12`;


create table article(
                        id int(10) unsigned not null primary key auto_increment,
                        regDate datetime not null,
                        updateDate datetime not null,
                        title char(100) not null,
                        `body` text not null
);

desc article;

select *
from article;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = CONCAT('제목', SUBSTRING(RAND() * 1000 FROM 1 FOR 2)),
`body` = CONCAT('내용', SUBSTRING(RAND() * 1000 FROM 1 FOR 2));

select now();

select '제목1';

select concat('제목','2');

select substring(RAND() * 1000 from 1 for 2);