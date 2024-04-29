CREATE TABLE review (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        rating INT,
                        comment VARCHAR(255),
                        user_id BIGINT,
                        object_id BIGINT,
                        FOREIGN KEY (user_id) REFERENCES user(id),
                        FOREIGN KEY (object_id) REFERENCES recreation_object(id)
);