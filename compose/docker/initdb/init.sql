-- Tạo database
CREATE DATABASE IF NOT EXISTS project1;
USE project1;

-- Tạo bảng users
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(128),
    enabled INT NOT NULL DEFAULT 1,
    phone_number VARCHAR(15),
    email VARCHAR(128),
    birthday DATE,
    address VARCHAR(256),
    employee_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng roles
CREATE TABLE IF NOT EXISTS roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    code VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(256),
    enabled INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng employees
CREATE TABLE IF NOT EXISTS employees (
    employee_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    department_id BIGINT NOT NULL,
    enabled INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng departments
CREATE TABLE IF NOT EXISTS departments (
    department_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    code VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(256),
    address VARCHAR(256),
    enabled INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng documents
CREATE TABLE IF NOT EXISTS documents (
    document_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type_id BIGINT NOT NULL,
    code VARCHAR(64),
    content VARCHAR,
    stype_id BIGINT,
    status_id BIGINT,
    signer_id BIGINT,
    urgency_id BIGINT,
    department_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng document_type
CREATE TABLE IF NOT EXISTS document_type (
    type_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR,
    enabled INT(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng document_stype
CREATE TABLE IF NOT EXISTS document_stype (
    stype_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR,
    enabled INT(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng document_receiver_map
CREATE TABLE IF NOT EXISTS document_receiver_map (
    document_receiver_map_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    receiver_date DATE,
    promulgate_date DATE,
    deadline_date DATE,
    comment VARCHAR,
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng document_status
CREATE TABLE IF NOT EXISTS document_status (
    status_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR,
    enabled INT(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Tạo bảng document_status
CREATE TABLE IF NOT EXISTS document_urgency (
    urgency_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    content VARCHAR,
    enabled INT(1) DEFAULT 1 NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(128),
    updated_at TIMESTAMP,
    updated_by VARCHAR(128),
    del_flag INT(1),
    deleted_by VARCHAR(128)
);

-- Thêm dữ liệu mẫu vào users (Password: admin123@ or linh123@ or ngoan123@)
INSERT INTO users (username, password, full_name, email, employee_id, phone_number) VALUES
('admin', '$2a$10$fHP/6cFcBoBcj0.cB/1cIO7W.ZC2vE5SVa5opNK9zIbNUxQ47IqZy', 'Administrator', 'admin123@gmail.com', 1, 0123456789),
('linh.vt', '$2a$10$T4Y9tEDoLi8VFjnoJ2JcsOeV394ebH6MX2v66BwrOOR0CIF.PtUC.', 'Document Manager Linh', 'linh.vt123@gmail.com', 2, 0123456789),
('linh.cv', '$2a$10$T4Y9tEDoLi8VFjnoJ2JcsOeV394ebH6MX2v66BwrOOR0CIF.PtUC.', 'Specialist Linh', 'linh.cv123@gmail.com', 3, 0123456789),
('linh.tl', '$2a$10$T4Y9tEDoLi8VFjnoJ2JcsOeV394ebH6MX2v66BwrOOR0CIF.PtUC.', 'Assistant Linh', 'linh.tl123@gmail.com', 4, 0123456789),
('linh.ld', '$2a$10$T4Y9tEDoLi8VFjnoJ2JcsOeV394ebH6MX2v66BwrOOR0CIF.PtUC.', 'Leader Linh', 'linh.ld123@gmail.com', 5, 0123456789),
('ngoan.vt', '$2a$10$1P.oy6dm5.K/3I3JySjjr.iEtOyxwEYupstkG9CZcspjWTM38sPGS', 'Document Manager Ngoan', 'ngoan.vt123@gmail.com', 6, 0123456789),
('ngoan.cv', '$2a$10$1P.oy6dm5.K/3I3JySjjr.iEtOyxwEYupstkG9CZcspjWTM38sPGS', 'Specialist Ngoan', 'ngoan.cv123@gmail.com', 7, 0123456789),
('ngoan.tl', '$2a$10$1P.oy6dm5.K/3I3JySjjr.iEtOyxwEYupstkG9CZcspjWTM38sPGS', 'Assistant Ngoan', 'ngoan.tl123@gmail.com', 8, 0123456789),
('ngoan.ld', '$2a$10$1P.oy6dm5.K/3I3JySjjr.iEtOyxwEYupstkG9CZcspjWTM38sPGS', 'Leader Ngoan', 'ngoan.ld123@gmail.com', 9, 0123456789),
('ninh.vt', '$2a$10$.sogbqn4CSKNs0GOTa519.mRX/DrUgHmbiKeNyjHOeFijM99T4/h6', 'Document Manager Ninh', 'ninh.vt123@gmail.com', 10, 0123456789),
('ninh.cv', '$2a$10$.sogbqn4CSKNs0GOTa519.mRX/DrUgHmbiKeNyjHOeFijM99T4/h6', 'Specialist Ninh', 'ninh.cv123@gmail.com', 11, 0123456789),
('ninh.tl', '$2a$10$.sogbqn4CSKNs0GOTa519.mRX/DrUgHmbiKeNyjHOeFijM99T4/h6', 'Assistant Ninh', 'ninh.tl123@gmail.com', 12, 0123456789),
('ninh.ld', '$2a$10$.sogbqn4CSKNs0GOTa519.mRX/DrUgHmbiKeNyjHOeFijM99T4/h6', 'Leader Ninh', 'ninh.ld123@gmail.com', 13, 0123456789);

-- Thêm dữ liệu mẫu vào roles
INSERT INTO roles (name, code, description) VALUES
('ADMIN', 'QT', 'Quản trị toàn quyền'),
('DOC_MANAGER', 'VT', 'Văn thư đơn vị'),
('SPECIALIST', 'CV', 'Chuyên viên đơn vị'),
('ASSISTANT', 'TL', 'Trợ lý lãnh đạo'),
('LEADER', 'LD', 'Lãnh đạo đơn vị');

-- Thêm dữ liệu mẫu vào departments
INSERT INTO departments (name, code) VALUES
('Các cơ quản Đảng', 'ccqd'),
('Văn phòng trung ương Đảng', 'vptwd'),
('Tỉnh ủy Hà Tây', 'tuht'),
('Thành ủy Hồ Chí Minh', 'tuhcm');

-- Thêm dữ liệu mẫu vào employees
INSERT INTO employees (user_id, role_id, department_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 2),
(4, 4, 2),
(5, 5, 2),
(6, 2, 3),
(7, 3, 3),
(8, 4, 3),
(9, 5, 3),
(10, 2, 4),
(11, 3, 4),
(12, 4, 4),
(13, 5, 4);

-- Thêm dữ liệu mẫu vào document_type
INSERT INTO document_type (name, content) VALUES
('Văn bản thường', 'Các văn bản bình thường'),
('Văn bản trình ký', 'Các văn bản trình ký'),
('Văn bản tham khảo', 'Các văn bản tham khảo'),
('Báo cáo thường', 'Các báo cáo bình thường'),
('Doanh số', 'Các báo cáo doanh số'),
('Kế hoạch', 'Các văn bản kế hoạch'),
('Biểu mẫu', 'Các biểu mẫu thường');

-- Thêm dữ liệu mẫu vào document_status
INSERT INTO document_status (name, content) VALUES
('Chờ xử lý', 'Văn bản chưa được xử lý'),
('Đã xử lý', 'Văn bản đã được xử lý'),
('Chờ tiếp nhận', 'Văn bản chưa được tiếp nhận'),
('Đã ban hành', 'Văn bản đã được ban hành'),
('Từ chối tiếp nhận', 'Văn bản bị từ chối tiếp nhận');

-- Thêm dữ liệu mẫu vào document_stype
INSERT INTO document_stype (name, content) VALUES
('Thường', 'Văn bản thường'),
('Mật', 'Văn bản mật'),
('Tuyệt mật', 'Văn bản tuyệt mật'),
('Tối mật', 'Văn bản tối mật');

-- Thêm dữ liệu mẫu vào document_urgency
INSERT INTO document_urgency (name, content) VALUES
('Thường', 'Văn bản có độ khẩn thường'),
('Hỏa tốc', 'Văn bản có độ khẩn hỏa tốc'),
('Khẩn cấp', 'Văn bản có độ khẩn cao nhât');

-- Thêm dữ liệu mẫu vào documents
INSERT INTO documents (type_id, code, content, stype_id, status_id, urgency_id, department_id) VALUES
(1, '11/aa', 'test chuyen vb 01', 1, null, 1, 2),
(2, '22-test', 'test chuyen vb 02', 1, null, 2, 3),
(3, 'cc', 'test chuyen vb 03', 1, null, 1, 4),
(1, '11/aa', 'test sua vb 01', 1, null, 1, 2),
(2, '22-test', 'test sua vb 02', 1, null, 2, 3),
(3, 'cc', 'test sua vb 03', 1, null, 1, 4),
(1, '11/aa', 'test xu ly vb 01', 1, null, 1, 2),
(2, '22-test', 'test xu ly vb 02', 1, null, 2, 3),
(3, 'cc', 'test xu ly vb 03', 1, null, 1, 4),
(1, '11/aa', 'test xoa vb 01', 1, null, 1, 2),
(2, '22-test', 'test xoa vb 02', 1, null, 2, 3),
(3, 'cc', 'test xoa vb 03', 1, null, 1, 4);
