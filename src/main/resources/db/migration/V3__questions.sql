CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    unit_id BIGINT NOT NULL,
    answered BOOLEAN NOT NULL,
    answer VARCHAR(5000),
    CONSTRAINT fk_question_unit FOREIGN KEY (unit_id) REFERENCES units (id)
);

ALTER TABLE notes ADD COLUMN question_id BIGINT;
ALTER TABLE notes ADD CONSTRAINT fk_note_question FOREIGN KEY (question_id) REFERENCES questions (id);