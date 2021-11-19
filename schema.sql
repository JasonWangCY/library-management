use db42;

create table if not exists user_category(
    ucid SMALLINT UNSIGNED NOT NULL,
    max SMALLINT UNSIGNED NOT NULL,
    period SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY(ucid)
);

create table if not exists libuser(
  libuid CHAR(10) NOT NULL,  
  name VARCHAR(25) NOT NULL,
  age SMALLINT UNSIGNED NOT NULL,
  address VARCHAR(100) NOT NULL,
  ucid SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY (libuid),
  FOREIGN KEY (ucid) REFERENCES user_category(ucid) ON UPDATE CASCADE ON DELETE CASCADE
);

create table if not exists book_category(
	bcid SMALLINT UNSIGNED NOT NULL,
    bcname VARCHAR(30) NOT NULL,
    PRIMARY KEY (bcid)
);

create table if not exists book(
    callnum CHAR(8) NOT NULL,
    title VARCHAR(30) NOT NULL,
    publish CHAR(10) NOT NULL,
    rating REAL UNSIGNED DEFAULT NULL,
    tborrowed SMALLINT UNSIGNED NOT NULL,
    bcid SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY (callnum),
    FOREIGN KEY(bcid) REFERENCES book_category(bcid) ON UPDATE CASCADE ON DELETE CASCADE
);

create table if not exists authorship(
    aname VARCHAR(255) NOT NULL,
    callnum CHAR(8) NOT NULL,
    PRIMARY KEY (aname, callnum),
    FOREIGN KEY (callnum) REFERENCES book(callnum)
);

create table if not exists copy(
    copynum SMALLINT UNSIGNED NOT NULL,
    callnum CHAR(8) NOT NULL,
    PRIMARY KEY (copynum, callnum),
    FOREIGN KEY (callnum) REFERENCES book(callnum) ON UPDATE CASCADE ON DELETE CASCADE 
);

create table if not exists borrow(
    libuid CHAR(10) NOT NULL,
    callnum CHAR(8) NOT NULL,
    copynum SMALLINT UNSIGNED NOT NULL,
    checkout CHAR(10) NOT NULL,
    `return` CHAR(10) DEFAULT NULL,
    PRIMARY KEY (callnum, copynum, libuid, checkout),
    FOREIGN KEY(libuid) REFERENCES libuser(libuid) ON UPDATE CASCADE ON DELETE CASCADE, 
    FOREIGN KEY(callnum) REFERENCES book(callnum),
    FOREIGN KEY(copynum, callnum) REFERENCES copy(copynum, callnum) ON UPDATE CASCADE ON DELETE CASCADE
);
