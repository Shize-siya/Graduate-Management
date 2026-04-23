-- 创建数据库
CREATE DATABASE IF NOT EXISTS graduate_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE graduate_management;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 考生表
CREATE TABLE IF NOT EXISTS candidates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    id_card VARCHAR(18) NOT NULL UNIQUE,
    gender VARCHAR(10) NOT NULL,
    birth_date VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(200),
    graduate_school VARCHAR(100),
    graduate_major VARCHAR(100),
    graduation_year VARCHAR(4),
    political_status VARCHAR(50),
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 报考信息表
CREATE TABLE IF NOT EXISTS applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    exam_year VARCHAR(4) NOT NULL,
    first_choice_school VARCHAR(100) NOT NULL,
    first_choice_major VARCHAR(100) NOT NULL,
    second_choice_school VARCHAR(100),
    second_choice_major VARCHAR(100),
    exam_subject1 VARCHAR(100),
    exam_subject2 VARCHAR(100),
    exam_subject3 VARCHAR(100),
    exam_subject4 VARCHAR(100),
    initial_exam_score INT,
    recheck_exam_score INT,
    status VARCHAR(20),
    exam_location VARCHAR(100),
    exam_date VARCHAR(20),
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id)
);

-- 录取信息表
CREATE TABLE IF NOT EXISTS admissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    application_id BIGINT,
    admission_year VARCHAR(4) NOT NULL,
    admitted_school VARCHAR(100) NOT NULL,
    admitted_major VARCHAR(100) NOT NULL,
    admission_type VARCHAR(20),
    supervisor VARCHAR(50),
    admission_score INT,
    total_score INT,
    status VARCHAR(20),
    enrollment_date VARCHAR(20),
    tuition_standard DOUBLE,
    scholarship_level VARCHAR(20),
    remarks TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES candidates(id),
    FOREIGN KEY (application_id) REFERENCES applications(id)
);

-- 插入默认管理员账户 (密码: admin123)
INSERT INTO users (username, password, role, email)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'ADMIN', 'admin@example.com')
ON DUPLICATE KEY UPDATE username = username;
