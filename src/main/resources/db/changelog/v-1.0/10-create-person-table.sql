CREATE TABLE person (
                        user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        full_name VARCHAR(255),
                        gender VARCHAR(255),
                        FOREIGN KEY (user_id) REFERENCES user(id)
);