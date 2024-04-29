CREATE TABLE booking (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT,
                         object_id BIGINT,
                         booking_start_time DATE,
                         booking_end_time DATE,
                         FOREIGN KEY (user_id) REFERENCES user(id),
                         FOREIGN KEY (object_id) REFERENCES recreation_object(id)
);