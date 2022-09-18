CREATE TABLE TRIP (
  id VARCHAR(255) PRIMARY KEY,
  origin_airport_code VARCHAR(255) NOT NULL,
  destination_airport_code VARCHAR(255) NOT NULL,
  departure_date VARCHAR(255) NOT NULL,
  return_date VARCHAR(255) NOT NULL
);
