CREATE DATABASE IF NOT EXISTS cooperative_db;
USE cooperative_db;

CREATE TABLE IF NOT EXISTS agenda (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      title VARCHAR(200) NOT NULL UNIQUE,
                                      description VARCHAR(500),
                                      created_at DATETIME,
                                      start_time DATETIME,
                                      end_time DATETIME
);

CREATE TABLE IF NOT EXISTS vote (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    user_id BIGINT NOT NULL,
                                    vote_type ENUM('YES','NO') NOT NULL,
                                    agenda_id BIGINT NOT NULL,
                                    CONSTRAINT fk_agenda FOREIGN KEY (agenda_id) REFERENCES agenda(id) ON DELETE CASCADE,
                                    UNIQUE KEY unique_vote_per_user (agenda_id, user_id)
)
