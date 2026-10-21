CREATE TABLE signers
(
    id            UUID                        PRIMARY KEY ,
    document_id   UUID                        NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    name          VARCHAR(255)                NOT NULL,
    email         VARCHAR(255)                NOT NULL,
    signing_order INT                     NOT NULL,
    status        VARCHAR(255)                NOT NULL DEFAULT 'PENDING',
    created_at    timestamptz NOT NULL DEFAULT now(),
    signed_at     timestamptz,
    CONSTRAINT uq_signers_document_email UNIQUE (document_id, email),
    CONSTRAINT chk_signers_signing_order CHECK (signing_order >= 1)
);

CREATE INDEX idx_signers_document_id ON signers (document_id);