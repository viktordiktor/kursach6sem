CREATE TABLE photo (
                       photo_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       object_id BIGINT,
                       photo VARCHAR(255),
                       FOREIGN KEY (object_id) REFERENCES recreation_object(id)
);