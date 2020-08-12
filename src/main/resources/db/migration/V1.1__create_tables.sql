CREATE TABLE IF NOT EXISTS rides_logs(
    id_ride_log serial,
    id_ride bigint,
	state varchar(255),
    created_at TIMESTAMP,
    PRIMARY KEY (id_ride_log)
);