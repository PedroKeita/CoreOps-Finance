CREATE TABLE audit_log (
    id         BIGSERIAL    PRIMARY KEY,
    user_id    BIGINT       REFERENCES users(id) ON DELETE SET NULL,
    action     VARCHAR(50)  NOT NULL,
    entity     VARCHAR(100) NOT NULL,
    entity_id  BIGINT,
    timestamp  TIMESTAMP    NOT NULL DEFAULT NOW()
);