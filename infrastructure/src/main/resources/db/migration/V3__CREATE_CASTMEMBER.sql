CREATE TABLE cast_members (
  id CHAR(32) NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  type VARCHAR(32) NOT NULL,
  created_at TIMESTAMP(9) NOT NULL,
  updated_at TIMESTAMP(9) NOT NULL
);