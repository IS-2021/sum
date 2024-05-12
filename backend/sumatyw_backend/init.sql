CREATE DATABASE food_good;

CREATE TABLE cities (
                      city_id UUID NOT NULL DEFAULT UUID() PRIMARY KEY,
                      country varchar(255) DEFAULT NULL,
                      name varchar(255) DEFAULT NULL,
                      region varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

LOAD DATA INFILE '/home/wojewodztwa_miasta.csv'
INTO TABLE cities
CHARACTER SET cp1250
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 LINES (name, region);

INSERT INTO cities(country,name,region) VALUES('Poland','Warszawa','MAZOWIECKIE');
UPDATE cities
SET region = TRIM(BOTH '\r' FROM region)
WHERE region LIKE '%\r%';

UPDATE cities SET country = 'Poland';
