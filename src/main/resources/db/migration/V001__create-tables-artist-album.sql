CREATE TABLE artist (
    id SERIAL PRIMARY KEY,
    name CHARACTER VARYING(128) NOT NULL,
    UNIQUE (name)
);

CREATE TABLE album (
    id SERIAL PRIMARY KEY,
    name CHARACTER VARYING(128) NOT NULL,
    artist_id INTEGER NOT NULL,
    UNIQUE (name),
    FOREIGN KEY (artist_id) REFERENCES artist (id)
);

CREATE TABLE cover (
	id SERIAL PRIMARY KEY,
	object_name UUID NOT NULL,
	album_id INTEGER NOT NULL,
	FOREIGN KEY (album_id) REFERENCES album (id)
);