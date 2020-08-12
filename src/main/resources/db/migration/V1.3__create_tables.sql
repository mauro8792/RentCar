ALTER TABLE vehicle_categories DROP COLUMN commission;

ALTER TABLE providers ADD COLUMN commission DOUBLE PRECISION;

CREATE TABLE IF NOT EXISTS sales (
	id_sale bigserial,
	id_ride bigint,
	profit double precision,
	created_at timestamp,
	PRIMARY KEY (id_sale),
	CONSTRAINT FK_rides_sales FOREIGN KEY (id_ride) REFERENCES rides (id_ride)
);