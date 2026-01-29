-- Ensure unique constraints exist for idempotent seeding
DO $$
BEGIN
    ALTER TABLE organizations ADD CONSTRAINT uk_organizations_tin UNIQUE (tin);
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

DO $$
BEGIN
    ALTER TABLE users ADD CONSTRAINT uk_users_pin UNIQUE (pin);
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

DO $$
BEGIN
    ALTER TABLE asan_user_certificates ADD CONSTRAINT uk_user_voen UNIQUE (user_login_id, voen);
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

-- Seed Permissions (from Module enum)
INSERT INTO permissions (name, label, created_by, created_on, last_modified_by, last_modified_on)
VALUES ('VIEW_DASHBOARD', 'İdarəetmə paneli', 'system', NOW(), 'system', NOW()),
       ('VIEW_PRODUCTS', 'Məhsullar', 'system', NOW(), 'system', NOW()),
       ('VIEW_DST', 'DST', 'system', NOW(), 'system', NOW()),
       ('VIEW_DEMANDS', 'Tələbatlar', 'system', NOW(), 'system', NOW()),
       ('VIEW_SUPPLIERS', 'Satıcılar', 'system', NOW(), 'system', NOW()),
       ('VIEW_OFFER_PLACEMENT', 'Təklifin yerləşdirilməsi', 'system', NOW(), 'system', NOW()),
       ('VIEW_MONITORING', 'Monitorinq', 'system', NOW(), 'system', NOW()),
       ('VIEW_CONTRACTS', 'Müqavilələr', 'system', NOW(), 'system', NOW()),
       ('VIEW_TRANSACTIONS', 'Əməliyyatlar', 'system', NOW(), 'system', NOW()),
       ('VIEW_FINANCE', 'Maliyyə', 'system', NOW(), 'system', NOW()),
       ('VIEW_ARCHIVE', 'Arxiv', 'system', NOW(), 'system', NOW()),
       ('VIEW_REPORTS', 'Hesabatlar', 'system', NOW(), 'system', NOW()),
       ('VIEW_QA', 'Sual-cavab', 'system', NOW(), 'system', NOW()),
       ('VIEW_COMPLAINTS', 'Şikayətlər', 'system', NOW(), 'system', NOW()),
       ('VIEW_SETTINGS', 'Tənzimləmələr', 'system', NOW(), 'system', NOW())
ON CONFLICT (name) DO UPDATE SET label = EXCLUDED.label;

-- Seed Organizations (real and readable names in Azerbaijani)
INSERT INTO organizations (id, name, tin, address, phone_number, email, organization_type, is_registered, created_by, created_on)
VALUES (gen_random_uuid(), 'Aqrar Tədarük və Təchizat', '1001101112', 'Bakı şəhəri, Nərimanov r-nu, Ağa Nemətulla 107', '0124042020', 'info@aztelekom.az', 0, true, 'system', NOW()),
       (gen_random_uuid(), 'Azərbaycan düyü sənayə zavodu', '2002202223', 'Bakı şəhəri, Nəsimi r-nu, Dilarə Əliyeva 230', '0124997000', 'info@railway.gov.az', 1, true, 'system', NOW()),
       (gen_random_uuid(), 'Sumqayıt şəhər uşaq bağçası', '3003303334', 'Bakı şəhəri, Nərimanov r-nu, Ağa Nemətulla 2', '0124409000', 'info@azerenerji.gov.az', 2, true, 'system', NOW())
ON CONFLICT (tin) DO NOTHING;

-- Seed Roles for Org 1 (Aztelekom MMC)
-- Note: id is BIGINT identity, we seed with specific IDs for later reference
INSERT INTO roles (id, name, organization_id, is_default, is_from_system, created_by, created_on)
VALUES (1, 'ADMIN', (SELECT id FROM organizations WHERE tin = '1001101112' LIMIT 1), false, false, 'system', NOW()),
       (2, 'MANAGER', (SELECT id FROM organizations WHERE tin = '1001101112' LIMIT 1), false, false, 'system', NOW()),
       (3, 'OPERATOR', (SELECT id FROM organizations WHERE tin = '1001101112' LIMIT 1), false, false, 'system', NOW())
ON CONFLICT (id) DO NOTHING;

-- Seed Roles for Org 2 (Azərbaycan Dəmir Yolları QSC)
INSERT INTO roles (id, name, organization_id, is_default, is_from_system, created_by, created_on)
VALUES (4, 'ADMIN', (SELECT id FROM organizations WHERE tin = '2002202223' LIMIT 1), false, false, 'system', NOW()),
       (5, 'MANAGER', (SELECT id FROM organizations WHERE tin = '2002202223' LIMIT 1), false, false, 'system', NOW()),
       (6, 'OPERATOR', (SELECT id FROM organizations WHERE tin = '2002202223' LIMIT 1), false, false, 'system', NOW())
ON CONFLICT (id) DO NOTHING;

-- Seed Roles for Org 3 (Azərenerji ASC)
INSERT INTO roles (id, name, organization_id, is_default, is_from_system, created_by, created_on)
VALUES (7, 'ADMIN', (SELECT id FROM organizations WHERE tin = '3003303334' LIMIT 1), false, false, 'system', NOW()),
       (8, 'MANAGER', (SELECT id FROM organizations WHERE tin = '3003303334' LIMIT 1), false, false, 'system', NOW()),
       (9, 'OPERATOR', (SELECT id FROM organizations WHERE tin = '3003303334' LIMIT 1), false, false, 'system', NOW())
ON CONFLICT (id) DO NOTHING;

-- Link Roles and Permissions
INSERT INTO role_permission (role_id, permission_id)
SELECT role_id, name
FROM permissions
CROSS JOIN (VALUES (1), (4), (7)) AS admin_roles(role_id)
ON CONFLICT DO NOTHING; -- ADMIN roles have all

INSERT INTO role_permission (role_id, permission_id)
VALUES (2, 'VIEW_DASHBOARD'), (2, 'VIEW_DEMANDS'), (2, 'VIEW_SETTINGS'), (2, 'VIEW_PRODUCTS')
ON CONFLICT DO NOTHING; -- MANAGER

INSERT INTO role_permission (role_id, permission_id)
VALUES (3, 'VIEW_DASHBOARD'), (3, 'VIEW_DEMANDS')
ON CONFLICT DO NOTHING; -- OPERATOR

INSERT INTO role_permission (role_id, permission_id)
VALUES (5, 'VIEW_DASHBOARD'), (5, 'VIEW_DEMANDS'), (5, 'VIEW_SETTINGS'), (5, 'VIEW_PRODUCTS')
ON CONFLICT DO NOTHING; -- MANAGER (Org 2)

INSERT INTO role_permission (role_id, permission_id)
VALUES (6, 'VIEW_DASHBOARD'), (6, 'VIEW_DEMANDS')
ON CONFLICT DO NOTHING; -- OPERATOR (Org 2)

INSERT INTO role_permission (role_id, permission_id)
VALUES (8, 'VIEW_DASHBOARD'), (8, 'VIEW_DEMANDS'), (8, 'VIEW_SETTINGS'), (8, 'VIEW_PRODUCTS')
ON CONFLICT DO NOTHING; -- MANAGER (Org 3)

INSERT INTO role_permission (role_id, permission_id)
VALUES (9, 'VIEW_DASHBOARD'), (9, 'VIEW_DEMANDS')
ON CONFLICT DO NOTHING; -- OPERATOR (Org 3)

-- Seed Users
INSERT INTO users (id, phone_number, pin, first_name, last_name, patronymic, citizenship, last_active_time)
VALUES (gen_random_uuid(), '0501112233', 'AZE1234567', 'Cəmal', 'Cavadov', 'Elxan', 'AZ', NOW()),
       (gen_random_uuid(), '0702223344', 'AZE2345678', 'Aysel', 'Həsənova', 'Rauf', 'AZ', NOW()),
       (gen_random_uuid(), '0773334455', 'AZE3456789', 'Orxan', 'Məmmədov', 'Samir', 'AZ', NOW())
ON CONFLICT (pin) DO NOTHING;

-- Seed Certificates
INSERT INTO asan_user_certificates (id, user_login_id, voen, certificat_number, has_stamp, legal, structure_name, position, organization_id)
VALUES (gen_random_uuid(), (SELECT id FROM users WHERE pin = 'AZE1234567'), '1001101112', 'CERT-0001', true, true, 'İT və İnfrastruktur şöbəsi', 'Direktor',
        (SELECT id FROM organizations WHERE tin = '1001101112' LIMIT 1)),
       (gen_random_uuid(), (SELECT id FROM users WHERE pin = 'AZE2345678'), '2002202223', 'CERT-0002', true, true, 'Satınalma şöbəsi', 'Şöbə müdiri',
        (SELECT id FROM organizations WHERE tin = '2002202223' LIMIT 1)),
       (gen_random_uuid(), (SELECT id FROM users WHERE pin = 'AZE3456789'), '3003303334', 'CERT-0003', true, true, 'Təchizat şöbəsi', 'Mütəxəssis',
        (SELECT id FROM organizations WHERE tin = '3003303334' LIMIT 1))
ON CONFLICT (user_login_id, voen) DO NOTHING;

-- Backfill organization_id for existing certificates (repeatable migration safety)
UPDATE asan_user_certificates c
SET organization_id = o.id
FROM organizations o
WHERE c.organization_id IS NULL
  AND c.voen = o.tin;

-- Assign User Roles
INSERT INTO user_role (role_id, asan_user_certificate_id, tin)
VALUES (1, (SELECT id FROM asan_user_certificates WHERE certificat_number = 'CERT-0001' LIMIT 1), '1001101112'),
       (2, (SELECT id FROM asan_user_certificates WHERE certificat_number = 'CERT-0001' LIMIT 1), '1001101112'),
       (5, (SELECT id FROM asan_user_certificates WHERE certificat_number = 'CERT-0002' LIMIT 1), '2002202223'),
       (9, (SELECT id FROM asan_user_certificates WHERE certificat_number = 'CERT-0003' LIMIT 1), '3003303334')
ON CONFLICT DO NOTHING;
