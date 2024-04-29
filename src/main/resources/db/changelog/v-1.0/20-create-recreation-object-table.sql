CREATE TABLE recreation_object (
                                   id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   object_type VARCHAR(255),
                                   name VARCHAR(255),
                                   available_guests INT,
                                   description TEXT,
                                   price_per_day DECIMAL(10, 2),
                                   main_photo VARCHAR(255)
);