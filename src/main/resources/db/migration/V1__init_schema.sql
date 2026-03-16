-- Schema migrated from backend/database/schema.sql

CREATE TABLE IF NOT EXISTS users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS applications (
  id SERIAL PRIMARY KEY,
  job_id VARCHAR(100) DEFAULT NULL,
  user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  company VARCHAR(255) NOT NULL,
  role VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL CHECK (status IN ('Applied', 'OA', 'Interview', 'Rejected', 'Offer')),
  status_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  applied_date DATE NOT NULL,
  salary DECIMAL(6,2) DEFAULT NULL,
  notes TEXT DEFAULT NULL,
  follow_up BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_applications_user_status ON applications(user_id, status);
CREATE INDEX IF NOT EXISTS idx_applications_applied_date ON applications(applied_date);
CREATE INDEX IF NOT EXISTS idx_applications_salary ON applications(salary);
CREATE INDEX IF NOT EXISTS idx_applications_role ON applications(role);

