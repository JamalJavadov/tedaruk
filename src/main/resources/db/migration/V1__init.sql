
-- AsanUserEntity
CREATE TABLE IF NOT EXISTS att_user_db.users (
	id UUID PRIMARY KEY,
	phone_number VARCHAR(50),
	pin VARCHAR(20) UNIQUE,
	first_name  VARCHAR(100),
	last_name VARCHAR(100),
	patronymic VARCHAR(100),
	citizenship VARCHAR(50),
	is_active BOOLEAN DEFAULT TRUE,
	role VARCHAR(50) DEFAULT 'seller',
	last_active_time TIMESTAMP
);

-- AsanUserCertificatesEntity
CREATE TABLE IF NOT EXISTS att_user_db.asan_user_certificates (
	id UUID PRIMARY KEY,
	user_login_id UUID NOT NULL,
	certificat_number VARCHAR(100),
	has_stamp BOOLEAN,
	legal BOOLEAN,
	voen VARCHAR(50),
	structure_name VARCHAR(255),
	position VARCHAR(100),
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT fk_user_login FOREIGN KEY (user_login_id) REFERENCES att_user_db.users(id),
	CONSTRAINT uk_user_voen UNIQUE (user_login_id, voen)
);

-- UserRole
CREATE TABLE IF NOT EXISTS user_role (
	id BIGSERIAL PRIMARY KEY,
	role_name VARCHAR(255) NOT NULL,
	voen VARCHAR(50) NOT NULL,
	asan_user_certificate_id UUID NOT NULL,
	is_active BOOLEAN DEFAULT TRUE,
	CONSTRAINT fk_asan_user_certificate FOREIGN KEY (asan_user_certificate_id)
		REFERENCES att_user_db.asan_user_certificates(id)
);
