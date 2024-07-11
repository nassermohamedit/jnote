CREATE TABLE roles (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(255)
);

INSERT INTO roles (name, description) VALUES ('admin', 'Administrator role');
INSERT INTO roles (name, description) VALUES ('user', 'Regular user role');

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL,
    role_id SMALLINT NOT NULL,
    creation_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY(role_id) REFERENCES roles(id)
);

create table modules (
    id BIGSERIAL PRIMARY KEY,
    description varchar(255),
    name varchar(255),
    owner_id bigint not null,
    creation_time TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT fk_module_owner FOREIGN KEY(owner_id) REFERENCES users(id),
    CONSTRAINT uq_user_module UNIQUE (name, owner_id)
);

create table notes (
    id BIGSERIAL PRIMARY KEY,
    content varchar(10000),
    module_id bigint not null,
    creation_time TIMESTAMP NOT NULL,
    CONSTRAINT fk_notes_modules FOREIGN KEY(module_id) REFERENCES modules(id)
);

