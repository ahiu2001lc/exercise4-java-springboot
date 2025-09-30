-- Tạo database
CREATE DATABASE IF NOT EXISTS authorization;
USE authorization;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(128),
    enabled INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tạo bảng roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE
);

-- Tạo bảng user_roles
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Thêm dữ liệu mẫu vào users
INSERT INTO users (username, password, full_name, enabled) VALUES
('admin', '$2a$10$fHP/6cFcBoBcj0.cB/1cIO7W.ZC2vE5SVa5opNK9zIbNUxQ47IqZy', 'Administrator', 1),
('user', '$2a$10$G8CTelHT27eMg1UJADaV/uxhnSpUFV2wCeP7iQZzR.mOxTo7YXKJK', 'Normal User', 1);

-- Thêm dữ liệu mẫu vào roles
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- Gán role cho từng user
INSERT INTO user_roles (user_id, role_id) VALUES
((SELECT id FROM users WHERE username='admin'),
 (SELECT id FROM roles WHERE name='ROLE_ADMIN')),
((SELECT id FROM users WHERE username='user'),
 (SELECT id FROM roles WHERE name='ROLE_USER'));
