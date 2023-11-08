INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DR-01', 'LIGHT_WEIGHT', 100, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DR-02', 'LIGHT_WEIGHT', 100, 100, 'LOADING');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DR-03', 'LIGHT_WEIGHT', 100, 100, 'IDLE');
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DR-04', 'LIGHT_WEIGHT', 100, 100, 'IDLE');



INSERT INTO medication (code, "name", weight, image)
VALUES ('01', 'ASPIRIN', 10, 'https://s3.amazonaws.com/bucket/1.png');
INSERT INTO medication (code, "name", weight, image)
VALUES ('02', 'VITAMIN', 10, 'https://s3.amazonaws.com/bucket/2.png');
INSERT INTO medication (code, "name", weight, image)
VALUES ('03', 'MELATONIN', 300, 'https://s3.amazonaws.com/bucket/3.png');
INSERT INTO medication (code, "name", weight, image)
VALUES ('04', 'BISACODIL', 100, 'https://s3.amazonaws.com/bucket/4.png');
INSERT INTO medication (code, "name", weight, image)
VALUES ('05', 'GUTALAKS', 150, 'https://s3.amazonaws.com/bucket/5.png');
INSERT INTO medication (code, "name", weight, image)
VALUES ('06', 'FITOLAKS', 400, 'https://s3.amazonaws.com/bucket/6.png');

INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DR-02', '01');