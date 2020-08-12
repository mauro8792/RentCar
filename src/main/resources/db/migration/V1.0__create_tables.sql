CREATE TABLE IF NOT EXISTS brands (
	id_brand bigserial,
	created_at timestamp,
	is_active boolean,
	name varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_brand),
	CONSTRAINT UK_brand_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS category_vehicles (
	id_category_vehicle bigserial,
	commission double precision,
	name varchar(255),
	PRIMARY KEY (id_category_vehicle),
	CONSTRAINT UK_category_vehicle_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS countries (
	id_country bigserial,
	created_at timestamp,
	name varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_country),
	CONSTRAINT UK_country_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS cities (
	id_city bigserial,
	id_country bigint,
	created_at timestamp,
	name varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_city),
	CONSTRAINT FK_cities_countries FOREIGN KEY (id_country) REFERENCES countries (id_country)
);

CREATE TABLE IF NOT EXISTS licenses (
	id_license bigserial,
	created_at timestamp,
	expiration_date timestamp,
	number varchar(255),
	updated_at timestamp,
	validated boolean,
	PRIMARY KEY (id_license),
	CONSTRAINT UK_license_number UNIQUE (number)
);

CREATE TABLE IF NOT EXISTS points (
	id_point bigserial,
	id_city bigint,
	capacity integer,
	created_at timestamp,
	is_active boolean,
	is_destination boolean,
	is_origin boolean,
	lat varchar(255),
	lng varchar(255),
	stock integer,
	updated_at timestamp,
	PRIMARY KEY (id_point),
	CONSTRAINT FK_points_cities FOREIGN KEY (id_city) REFERENCES cities (id_city)
);

CREATE TABLE IF NOT EXISTS users(
    id_user serial,
    id_license bigint,
    birth_date TIMESTAMP,
    created_at TIMESTAMP,
    dni varchar(255),
    is_active boolean,
    last_name varchar(255),
    name varchar(255),
    pwd varchar(255),
    updated_at TIMESTAMP,
    PRIMARY KEY (id_user),
    CONSTRAINT FK_users_licenses FOREIGN KEY (id_license) REFERENCES licenses (id_license)
);

CREATE TABLE IF NOT EXISTS providers (
	id_provider bigserial,
	is_active boolean,
	name varchar(255),
	business_name varchar(255),
	created_at timestamp,
	email varchar(255),
	password varchar(255),
	phone varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_provider),
	CONSTRAINT UK_providers_name UNIQUE (name),
	CONSTRAINT UK_providers_business_name UNIQUE (business_name),
	CONSTRAINT UK_providers_email UNIQUE (email),
	CONSTRAINT UK_providers_phone UNIQUE (phone)
);

CREATE TABLE IF NOT EXISTS vehicle_categories (
	id_category_vehicle bigserial,
	commission double precision,
	created_at timestamp,
	is_active boolean,
	name varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_category_vehicle),
	CONSTRAINT UK_vehicle_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS vehicle_models (
	id_vehicle_model bigserial,
	id_brand bigint,
	cant_place integer,
	created_at timestamp,
	is_active boolean,
	is_automatic boolean,
	name varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_vehicle_model),
	CONSTRAINT UK_vehicle_models_name UNIQUE (name),
	CONSTRAINT FK_brands_vehicle_models FOREIGN KEY (id_brand) REFERENCES brands (id_brand)
);

CREATE TABLE IF NOT EXISTS vehicles (
	id_vehicle bigserial,
	id_category_vehicle bigint,
	id_provider bigint,
	id_vehicle_model bigint,
	available boolean,
	color varchar(255),
	domain varchar(255),
	is_active boolean,
	PRIMARY KEY (id_vehicle),
	CONSTRAINT UK_vehicles_domain UNIQUE (domain),
	CONSTRAINT FK_category_vehicles_vehicles FOREIGN KEY (id_category_vehicle) REFERENCES vehicle_categories (id_category_vehicle),
	CONSTRAINT FK_providers_vehicles FOREIGN KEY (id_vehicle_model) REFERENCES vehicle_models (id_vehicle_model),
	CONSTRAINT FK_vehicle_models_vehicles FOREIGN KEY (id_provider) REFERENCES providers (id_provider)
);

CREATE TABLE IF NOT EXISTS rides (
	id_ride bigserial,
	id_origin_point bigint,
	id_destination_point bigint,
	id_user bigint,
	id_vehicle bigint,
	code varchar(255),
	created_at timestamp,
	end_date timestamp,
	price double precision,
	start_date timestamp,
	state varchar(255),
	tariff_type varchar(255),
	updated_at timestamp,
	PRIMARY KEY (id_ride),
	CONSTRAINT FK_destination_points_rides FOREIGN KEY (id_origin_point) REFERENCES points (id_point),
	CONSTRAINT FK_origin_points_rides FOREIGN KEY (id_vehicle) REFERENCES vehicles (id_vehicle),
	CONSTRAINT FK_users_rides FOREIGN KEY (id_destination_point) REFERENCES points (id_point),
	CONSTRAINT FK_vehicles_rides FOREIGN KEY (id_user) REFERENCES users (id_user)
);

CREATE TABLE IF NOT EXISTS mishaps (
	id_mishap bigserial,
	id_ride bigint,
	description varchar(255),
	title text,
	PRIMARY KEY (id_mishap),
	CONSTRAINT FK_mishaps_rides FOREIGN KEY (id_ride) REFERENCES rides (id_ride)
);
