ALTER TABLE asan_user_certificates ADD COLUMN organization_id UUID;

ALTER TABLE asan_user_certificates
    ADD CONSTRAINT fk_asan_user_certificates_on_organization FOREIGN KEY (organization_id) REFERENCES organizations (id);
