ALTER TABLE modules RENAME TO units;
ALTER SEQUENCE modules_id_seq RENAME TO units_id_seq;

CREATE TABLE modules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL,
    description VARCHAR(1000),
    creation_time TIMESTAMP NOT NULL,
    last_updated TIMESTAMP NOT NULL,
    CONSTRAINT fk_module_owner_ FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT uq_module UNIQUE (name, owner_id)
);

ALTER TABLE units ADD COLUMN module_id BIGINT;
ALTER TABLE units ADD CONSTRAINT fk_module_id FOREIGN KEY (module_id) REFERENCES modules (id);
ALTER TABLE units ADD COLUMN self BOOLEAN;

DO $$
DECLARE
    mod_id modules.id%TYPE;
    rec RECORD;
BEGIN
    FOR rec IN (SELECT * FROM units) LOOP
        INSERT INTO modules (name, owner_id, description, creation_time, last_updated)
        VALUES (rec.name, rec.owner_id, rec.description, rec.creation_time, rec.last_updated)
        RETURNING id INTO mod_id;
        UPDATE units SET module_id = mod_id, self = TRUE WHERE id = rec.id;
    END LOOP;
END $$;


ALTER TABLE units
    DROP CONSTRAINT fk_module_owner,
    DROP CONSTRAINT uq_user_module,
    DROP COLUMN owner_id,
    ALTER COLUMN module_id SET NOT NULL,
    ADD CONSTRAINT unique_unit UNIQUE (name, module_id),
    ALTER COLUMN self SET NOT NULL;

ALTER TABLE modules RENAME CONSTRAINT fk_module_owner_ TO fk_module_owner;
ALTER TABLE notes RENAME COLUMN module_id TO unit_id;